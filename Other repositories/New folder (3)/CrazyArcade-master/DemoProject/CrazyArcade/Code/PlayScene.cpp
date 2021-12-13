#include "PlayScene.h"
#include "Sprite.h"
#include "RoleState.h"
#include "LGInput.h"
#include <vector>
#include <algorithm>
#include "Bubble.h"
#include "MessageDispatcher.h"
#include "Prop.h"
#include "AI.h"

using namespace std;

PlayScene::PlayScene()
	:m_MapBlock(XLENGTH,vector<MapType::E_MapType>(YLENGTH))
{

}

PlayScene::~PlayScene()
{
	SetGameEnd(true);
}



void PlayScene::Enter(LGCenter*)
{
	//���ɵ�ͼ
	CreateMapBlock();

	//�����ܱ���ͼ
	Object* pBackGround = new Object(L"BackGround",Sprite(L"Resource\\BG.png"), LayoutType::E_GUIs, Point(0.0f,0.0f));
	InsertObject(pBackGround);

	//�����·
	Object* pRoad = new Object(L"Road",Sprite(L"Resource\\road.png"),LayoutType::E_Roads,Util::ORIGINPIX);
	InsertObject(pRoad);

	//���뷽��
	for (int x = 0; x != XLENGTH; x++)
	{
		for (int y = 0; y != YLENGTH; y++)
		{
			if (m_MapBlock[x][y] != MapType::E_None)
			{
				Block* pBlock = new Block(x,y,m_MapBlock[x][y]);
				InsertObject(pBlock);
			}
		}
	}

	//�����ɫ
	m_pPlayer = new Role(L"Player", 4, 7, Role::E_RedBaby);
	m_pPlayer->GetAbility()->SetBubbleNum(4); // ��������������AI����
	m_pPlayer->GetAbility()->SetRunSpeed(5); // ��������������AI����
	m_pEnemy = new AI(L"Enemy", 13, 1, Role::E_BrownPirate);
	InsertObject(m_pPlayer);
	InsertObject(m_pEnemy);
}

void PlayScene::Execute(LGCenter* lgCenter, float deltaTime)
{
	KeyboardControl();
	CollisionDetection(m_pPlayer, deltaTime);
	//CollisionDetection(m_pEnemy, deltaTime);
	vector<Object*> tempVec = HaveAllObject();

	for (vector<Object*>::const_iterator itr = tempVec.begin(); itr != tempVec.end();)
	{
		(*itr)->Update(deltaTime);
		++itr;
	}

	//��ɫλ���ƶ������ͼλ�øı䣬�Ա�AI��������ɫ
	if (m_pPlayer->MapPosChanged())
	{
		MapChanged(true);
		m_pPlayer->MapPosChanged(false);
	}
}

void PlayScene::Exit(LGCenter*)
{
	SetGameEnd(true);
}


void PlayScene::CreateMapBlock()
{

	//int copyMap[XLENGTH][YLENGTH] = 
	//{
	//	0,0,0,5,4,5,5,5,4,5,0,0,0,
	//	2,0,2,4,6,0,6,0,6,4,1,0,1,
	//	4,5,4,5,4,9,4,9,4,5,4,5,4,
	//	2,4,2,4,6,0,6,0,6,4,1,4,1,
	//	4,5,4,6,9,0,0,0,9,6,4,5,4,
	//	6,4,6,9,9,6,5,6,9,9,6,4,6,
	//	0,0,9,9,6,4,7,4,6,9,9,0,0,
	//	6,0,6,4,5,4,8,0,0,4,6,0,6,
	//	0,0,9,9,6,4,10,0,0,0,9,0,0,
	//	6,4,6,9,9,6,5,6,9,9,6,4,6,
	//	4,5,4,6,9,0,0,0,9,6,4,5,4,
	//	3,4,3,4,6,0,6,0,6,4,2,4,2,
	//	4,5,4,5,4,9,4,9,4,5,4,5,4,
	//	3,0,3,4,6,0,6,0,6,4,2,0,2,
	//	0,0,0,5,4,5,5,5,4,5,0,0,0
	//};

	int copyMap[XLENGTH][YLENGTH] = 
	{
		0,0,0,5,4,0,0,0,0,0,0,0,0,
		2,0,2,4,6,0,6,0,6,4,1,0,1,
		4,5,4,5,4,0,4,9,4,5,4,0,4,
		2,4,2,4,6,0,6,0,6,4,1,0,1,
		4,5,4,6,9,0,0,0,9,6,4,0,4,
		6,4,6,9,9,6,5,6,9,9,6,0,6,
		0,0,9,9,6,4,7,4,6,9,9,0,0,
		6,0,6,4,5,4,8,0,0,4,6,0,6,
		0,0,0,0,6,4,10,0,0,0,0,0,0,
		6,0,6,0,0,6,5,6,0,9,6,4,6,
		4,0,4,6,0,0,0,0,0,6,4,5,4,
		3,0,3,4,6,0,6,0,6,4,2,4,2,
		4,0,4,5,4,9,4,9,4,5,4,5,4,
		3,0,3,4,6,0,6,0,6,4,2,0,2,
		0,0,0,5,4,5,5,5,4,5,0,0,0
	};

	for (int x = 0; x != XLENGTH; x++)
	{
		for (int y = 0; y != YLENGTH; y++)
		{
			MapType::E_MapType mapType;
			switch(copyMap[x][y])
			{
			case 0:
				{
					mapType = MapType::E_None;
					break;
				}
			case 1:
				{
					mapType = MapType::E_RedHouse;
					break;
				}
			case 2:
				{
					mapType = MapType::E_BlueHouse;
					break;
				}
			case 3:
				{
					mapType = MapType::E_YellowHouse;
					break;
				}
			case 4:
				{
					mapType = MapType::E_TownBlockRed;
					break;
				}
			case 5:
				{
					mapType = MapType::E_TownBlockYellow;
					break;
				}
			case 6:
				{
					mapType = MapType::E_Tree;
					break;
				}
			case 7:
				{
					mapType = MapType::E_TownWindmill;
					break;
				}
			case 8:
				{
					mapType = MapType::E_TownWindmillAni;
					break;
				}
			case 9:
				{
					mapType = MapType::E_TownBox;
					break;
				}
			case 10:
				{
					mapType = MapType::E_Solid;
					break;
				}
			default:
				{
					mapType = MapType::E_None;
					break;
				}
			}
			m_MapBlock[x][y] = mapType;
		}
	}

	MapChanged(true);

}


void PlayScene::KeyboardControl()
{
	//�������������������������������������������������������������������������������
	StateMachine<Role>* const& roleStateMachine = m_pPlayer->GetStateMachine();

	static std::vector<Role::E_RoleDirection> DirectionSeque;

	bool
	keyUp = LGInput::Instance()->GetCurrentKey(LGInput::E_KeyUp),
	keyDown = LGInput::Instance()->GetCurrentKey(LGInput::E_KeyDown),
	keyLeft = LGInput::Instance()->GetCurrentKey(LGInput::E_KeyLeft),
	keyRight = LGInput::Instance()->GetCurrentKey(LGInput::E_KeyRight);

	bool
	PrevkeyUp = LGInput::Instance()->GetPrevKey(LGInput::E_KeyUp),
	PrevkeyDown = LGInput::Instance()->GetPrevKey(LGInput::E_KeyDown),
	PrevkeyLeft = LGInput::Instance()->GetPrevKey(LGInput::E_KeyLeft),
	PrevkeyRight = LGInput::Instance()->GetPrevKey(LGInput::E_KeyRight);

	bool KeyChange = false;

	//������
	if (keyUp && !PrevkeyUp)
	{
		DirectionSeque.push_back(Role::E_Up);
		KeyChange = true;
	}
	if (keyDown && !PrevkeyDown)
	{
		DirectionSeque.push_back(Role::E_Down);
		KeyChange = true;
	}
	if (keyLeft && !PrevkeyLeft)
	{
		DirectionSeque.push_back(Role::E_Left);
		KeyChange = true;
	}
	if (keyRight && !PrevkeyRight)
	{
		DirectionSeque.push_back(Role::E_Right);
		KeyChange = true;
	}

	//������
	if (!keyUp && PrevkeyUp)
	{
		DirectionSeque.erase(std::find(DirectionSeque.begin(),DirectionSeque.end(),Role::E_Up));
		KeyChange = true;
	}
	if (!keyDown && PrevkeyDown)
	{
		DirectionSeque.erase(std::find(DirectionSeque.begin(),DirectionSeque.end(),Role::E_Down));
		KeyChange = true;
	}
	if (!keyLeft && PrevkeyLeft)
	{
		DirectionSeque.erase(std::find(DirectionSeque.begin(),DirectionSeque.end(),Role::E_Left));
		KeyChange = true;
	}
	if (!keyRight && PrevkeyRight)
	{
		DirectionSeque.erase(std::find(DirectionSeque.begin(),DirectionSeque.end(),Role::E_Right));
		KeyChange = true;
	}



	//�������Ž������İ���

	int Size = DirectionSeque.size();

	if (KeyChange)
	{
		if (Size != 0)
		{
			switch(DirectionSeque[Size-1])
			{
			case Role::E_Up:
				{
					roleStateMachine->ChangeState(WalkUp::Instance());
					break;
				}
			case Role::E_Down:
				{
					roleStateMachine->ChangeState(WalkDown::Instance());
					break;
				}
			case Role::E_Left:
				{
					roleStateMachine->ChangeState(WalkLeft::Instance());
					break;
				}
			case Role::E_Right:
				{
					roleStateMachine->ChangeState(WalkRight::Instance());
					break;
				}
			}
		}
		else
		{
			if (KeyChange)
			{
				roleStateMachine->RevertToPreviousState();
				KeyChange = false;
				m_pPlayer->SpreadDirty();
			}
		}
		
	}


	//������������������������������������������������������������������������������ո��
	if (LGInput::Instance()->GetCurrentKey(LGInput::E_KeySpace) && 
		!LGInput::Instance()->GetPrevKey(LGInput::E_KeySpace) &&
		m_pPlayer->IsStandOnBubble())
	{
		LGInput* test = LGInput::Instance();
		int rolePosX = m_pPlayer->MapPosX();
		int rolePosY = m_pPlayer->MapPosY();
		wstring bubbleName = Util::CreateMapName(m_pPlayer->MapPosX(),
			m_pPlayer->MapPosY(),L"Bubble");
		if(m_pPlayer->GetAbility()->GetBubbleNum() >= 1)
		{
			Bubble* bubble = new Bubble(rolePosX,rolePosY,Bubble::E_Wait,Bubble::E_None,
				m_pPlayer->GetAbility()->GetPower(),m_pPlayer);
			InsertObject(bubble);
			ChangeMap(rolePosX,rolePosY,MapType::E_Bubble);
			m_pPlayer->SetStandOnBubble(true);
			m_pEnemy->SetStandOnBubble(true);
			g_pMessageMachine->MakeMessage(5.0f,m_pPlayer->GetObjID(),bubble->GetObjID(),Bubble::E_Explode,NULL);
			m_pPlayer->GetAbility()->Decrease(Ability::E_BubbleNum);
		}
	}
}

void PlayScene::CollisionDetection(Role* role, float deltaTime)
{

}

bool PlayScene::OnMessage(LGCenter* agent, const Telegram& msg)
{
	return false;
}

void PlayScene::ChangeMap(int posX, int posY, MapType::E_MapType blockType)
{
	if(!GetGameEnd())
		m_MapBlock[posX][posY] = blockType;
}

void PlayScene::Explosion(int x, int y, int power)
{
	bool UpEnd = false;
	bool DownEnd = false;
	bool LeftEnd = false;
	bool RightEnd = false;
	//����
	Bubble* bubble = new Bubble(x,y,Bubble::E_Center,Bubble::E_None);
	m_MapBlock[x][y] = MapType::E_Danger;
	InsertObject(bubble);
	for (int i = 0; i < power; )
	{
		Bubble::E_StateType stateType;
		if (++i != power)
		{
			stateType = Bubble::E_Middle;
		}
		else
			stateType = Bubble::E_Tail;

		//��
		if(!UpEnd && y + i < YLENGTH)
		{
			if(DestoryMapPos(x,y+i,stateType,Bubble::E_Up))
				UpEnd = true;
		}
		//��
		if(!DownEnd && y - i >= 0)
		{
			if(DestoryMapPos(x,y-i,stateType,Bubble::E_Down))
				DownEnd = true;
		}
		//��
		if(!LeftEnd && x - i >= 0)
		{
			if(DestoryMapPos(x-i,y,stateType,Bubble::E_Left))
				LeftEnd = true;
		}
		//��
		if(!RightEnd && x + i < XLENGTH)
		{
			if(DestoryMapPos(x+i,y,stateType,Bubble::E_Right))
				RightEnd = true;
		}
	}
}

GameObject* PlayScene::GetGameObject(int x, int y)
{
	for (vector<Object*>::const_iterator itr = HaveAllObject().begin(); itr != HaveAllObject().end(); ++itr)
	{
		GameObject* gameObj = static_cast<GameObject*>(*itr);
		if (gameObj->MapPosX() == x && gameObj->MapPosY() == y)
		{
			return gameObj;
		}
	}
	return NULL;
}

bool PlayScene::DestoryMapPos(int x, int y, Bubble::E_StateType stateType, Bubble::E_Direction direction)
{
	switch(m_MapBlock[x][y])
	{
	case MapType::E_TownBlockRed:
	case MapType::E_TownBlockYellow:
	case MapType::E_TownBox:
	case MapType::E_SandBox:
	case MapType::E_SandBlockBuff:
	case MapType::E_SandBlockYellow:
		{
			Bubble *bubble = new Bubble(x,y,stateType,direction);
			InsertObject(bubble);
			if(DeleteObject(Util::CreateMapName(x,y,L"Block")))
				return true;
			break;
		}
	case MapType::E_None:
		{
			Bubble *bubble = new Bubble(x,y,stateType,direction);
			InsertObject(bubble);
			return false;
		}
	case MapType::E_Bubble:
		{
			if(DeleteObject(Util::CreateMapName(x,y,L"Bubble")))
			{
				m_MapBlock[x][y] = MapType::E_Danger;
				return true;
			}
		}
	case MapType::E_Prop:
		{
			if(DeleteObject(Util::CreateMapName(x,y,L"Prop")))
			{
				Bubble *bubble = new Bubble(x, y, stateType, direction);
				InsertObject(bubble);
				m_MapBlock[x][y] = MapType::E_None;
				return false;
			}
		}
	default: //����ը
		{
			return true;
		}
	}


	return true;
}