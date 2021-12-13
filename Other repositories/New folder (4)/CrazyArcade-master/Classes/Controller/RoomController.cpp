#include "RoomController.h"
#include "Network/api_generated.h"
#include "Model/User.h"
#include "Scene/GameScene.h"

USING_NS_CC;
using namespace API;

bool RoomController::init()
{
    if (!Layer::init())
    {
        return false;
    }

    return true;
}

void RoomController::onEnter()
{
    gameStatus = 0;
    Layer::onEnter();
#ifdef NETWORK
    client = Client::getInstance();
    if (!client->isConnected())
    {
        client->connect();
    }
    if (userBoxes.size() > 0)
    {
        // second enter room, request room info update
        flatbuffers::FlatBufferBuilder builder;
        auto orc = CreateRoomInfoUpdate(builder);
        auto msg = CreateMsg(builder, MsgType::MsgType_RoomInfoUpdate, orc.Union());
        builder.Finish(msg);
        client->send(builder.GetBufferPointer(), builder.GetSize());
    }
    CLIENT_ON(MsgType_Welcome, RoomController::onWelcome);
    CLIENT_ON(MsgType_RoomInfoUpdate, RoomController::onRoomInfoUpdate);
    CLIENT_ON(MsgType_GameStatusChange, RoomController::onGameStatusChange);
    CLIENT_ON(MsgType_Chat, RoomController::onChat);
#endif // NETWORK
}

void RoomController::onExit()
{
#ifdef NETWORK
    if (gameStatus == 0) // player exit room directly
    {
        client->close();
    }
    client->clear();
    client = nullptr;
#endif // NETWORK
    Layer::onExit();
}

void RoomController::onWelcome(const void * msg)
{
    auto data = static_cast<const Welcome*>(msg);
    auto user = User::getInstance();
    user->setUID(data->uid()->str());

    {
        // name has been set before
        flatbuffers::FlatBufferBuilder builder;
        auto name = builder.CreateString(user->getName());
        auto orc = CreateGotIt(builder, name);
        auto msg = CreateMsg(builder, MsgType_GotIt, orc.Union());
        builder.Finish(msg);
        client->send(builder.GetBufferPointer(), builder.GetSize());
    }
    {
        // join room
        flatbuffers::FlatBufferBuilder builder;
        auto orc = CreateJoinRoom(builder);
        auto msg = CreateMsg(builder, MsgType_JoinRoom, orc.Union());
        builder.Finish(msg);
        client->send(builder.GetBufferPointer(), builder.GetSize());
    }
}

void RoomController::onRoomInfoUpdate(const void * msg)
{
    auto data = static_cast<const RoomInfoUpdate*>(msg);
    auto userVector = data->users();

    auto it1 = userVector->begin();
    auto it2 = userBoxes.begin();
    for (; it2 != userBoxes.end(); ++it2)
    {
        std::string name = "";
        int role = -1;
        bool isReady = false;

        if (it1 != userVector->end())
        {
            //auto uid = it1->uid()->str();
            name = it1->name()->str();
            role = it1->role();
            isReady = it1->isReady();

            ++it1;
        }

        (*it2)->setUserName(name);
        (*it2)->setRole(role);
        (*it2)->setReadyLabel(isReady);
        
    }
}

void RoomController::onGameStatusChange(const void * msg)
{
    auto data = static_cast<const GameStatusChange*>(msg);
    auto status = data->status();
    gameStatus = static_cast<int>(status);

    if (status == GameStatus::GameStatus_PENDING)
    {
        Director::getInstance()->pushScene(GameScene::createScene());
    }
}

void RoomController::onChat(const void * msg)
{
    auto data = static_cast<const Chat*>(msg);
    getParent()->getEventDispatcher()->dispatchCustomEvent("update_chat", const_cast<char *>(data->text()->c_str()));
}

void RoomController::sendChat(const std::string & text)
{
    flatbuffers::FlatBufferBuilder builder;
    auto str = builder.CreateString(text);
    auto orc = CreateChat(builder, str);
    auto msg = CreateMsg(builder, MsgType_Chat, orc.Union());
    builder.Finish(msg);

    client->send(builder.GetBufferPointer(), builder.GetSize());
}

void RoomController::onUserChangeRole(int role)
{
    flatbuffers::FlatBufferBuilder builder;
    auto orc = CreateUserChangeRole(builder, role);
    auto msg = CreateMsg(builder, MsgType_UserChangeRole, orc.Union());
    builder.Finish(msg);
    
    client->send(builder.GetBufferPointer(), builder.GetSize());
}


void RoomController::onUserChangeStats(bool isReady)
{
    auto stats = static_cast<int>(isReady);
    flatbuffers::FlatBufferBuilder builder;
    auto orc = CreateUserChangeStats(builder, stats);
    auto msg = CreateMsg(builder, MsgType_UserChangeStats, orc.Union());
    builder.Finish(msg);

    client->send(builder.GetBufferPointer(), builder.GetSize());
}
