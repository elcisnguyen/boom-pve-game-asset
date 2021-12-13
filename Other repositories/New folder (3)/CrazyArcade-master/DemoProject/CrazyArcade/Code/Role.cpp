#include "GameUtil.h"
#include "Role.h"
#include "RoleState.h"
#include "LGInput.h"
#include "Prop.h"
#include "PlayScene.h"
#include "LGCenter.h"
#include <string>
using namespace std;

Point Role::m_standOnBubble = Point(-1,-1);

Role::Role(std::wstring objName, int mapPosX, int mapPosY, E_RoleSpriteType spriteType)
	:GameObject(mapPosX, mapPosY), m_direction(E_Down), m_CanMove(true), m_FullyEnter(false)
{

	m_objName = objName;

	switch (spriteType)
	{
	case E_RedBaby:
	{
		m_currentSprite = Sprite(L"Resource\\Role1.png", 4, 6, 0, 0, 24.0f);
		break;
	}
	case E_BrownPirate:
	{
		m_currentSprite = Sprite(L"Resource\\Role2.png", 4, 6, 0, 0, 24.0f);
		break;
	}
	}

	m_Layout = LayoutType::E_Roles;

	m_ObjectType = ObjectType::E_ObjRole;

	m_pStateMachine = new StateMachine<Role>(this);
	m_pStateMachine->SetCurrentState(IdleDown::Instance());
	Point tempPoint = CalPixelPos(mapPosX, mapPosY);
	m_pixelPos = Point(tempPoint.GetX() - 4, tempPoint.GetY() - 2);
	UpdateMapPos();
	UpdateRectCollision();
	m_pAbility = new Ability();
	m_WalkSpeed = 200.0f + m_pAbility->GetRunSpeed() * 50;
}

void Role::Update(float deltaTime)
{
	m_pStateMachine->Update(deltaTime);
	m_WalkSpeed = 200.0f + m_pAbility->GetRunSpeed() * 50;
}

float const& Role::GetWalkSpeed() const
{
	return m_WalkSpeed;
}

void Role::SetWalkSpeed(float val)
{
	m_WalkSpeed = val;
}

StateMachine<Role>* const& Role::GetStateMachine() const
{
	return m_pStateMachine;
}

void Role::Move(float deltaTime)
{
	//1��������ײ���

	//��ǰ���Ϸ���λ��
	Point curFullyMap = CalPixelPos(MapPosX(), MapPosY());
	PlayScene* currentScene = static_cast<PlayScene*>(g_pLGCenter->GetCurrentScene());
	PlayScene::Map2DVec map = currentScene->MapBlock();
	const int DriftOffset = 20; //Ư��ƫ��
	//�ж�ǰ���ϰ���
	switch (m_direction)
	{
	case Role::E_Up:
	{
		if (GetRectCollision().GetYInt() == curFullyMap.GetYInt()) //����
		{
			RecoveDirect(); //�����ϴ�Ư��
			if(MapPosY() + 1 >= YLENGTH || (map[MapPosX()][MapPosY()+1] != MapType::E_None && map[MapPosX()][MapPosY()+1] != MapType::E_Prop)) //ײǽ���ƶ�	
				return;

			int XOffset = GetRectCollision().GetXInt() - curFullyMap.GetXInt();
			
			if ( MapPosX() + 1 < XLENGTH && MapPosY() + 1 < YLENGTH &&
				map[MapPosX()+1][MapPosY()+1] != MapType::E_None 
				&& XOffset > 0 && XOffset <= DriftOffset && XOffset != 0 ) //������Ư��
			{
				SetDirection(Role::E_Left);
			}
			else if ( MapPosX() - 1 >= 0 && MapPosY() + 1 < YLENGTH &&
				map[MapPosX() - 1][MapPosY() + 1] != MapType::E_None
				&& -XOffset > 0 && -XOffset <= DriftOffset && XOffset != 0) //������Ư��
			{
				SetDirection(Role::E_Right);
			}

		}
		break;
	}
	case Role::E_Down:
	{
		if (GetRectCollision().GetYInt() == curFullyMap.GetYInt()) //����
		{
			RecoveDirect(); //�����ϴ�Ư��
			if(MapPosY() - 1 < 0 ||(map[MapPosX()][MapPosY()-1] != MapType::E_None && map[MapPosX()][MapPosY()-1] != MapType::E_Prop)) //ײǽ���ƶ�	
				return;

			int XOffset = GetRectCollision().GetXInt() - curFullyMap.GetXInt();

			if (MapPosX() + 1 < XLENGTH && MapPosY() - 1 >= 0 &&
				map[MapPosX() + 1][MapPosY() - 1] != MapType::E_None
				&& XOffset > 0 && XOffset <= DriftOffset && XOffset != 0) //������Ư��
			{
				SetDirection(Role::E_Left);
			}
			else if (MapPosX() - 1 >= 0 && MapPosY() - 1 >= 0 &&
				map[MapPosX() - 1][MapPosY() - 1] != MapType::E_None
				&& -XOffset > 0 && -XOffset <= DriftOffset && XOffset != 0) //������Ư��
			{
				SetDirection(Role::E_Right);
			}

		}

		break;
	}
	case Role::E_Left:
	{	
		if (GetRectCollision().GetXInt() == curFullyMap.GetXInt() )//����		
		{
			RecoveDirect(); //�����ϴ�Ư��
			if(MapPosX() - 1 < 0  || (map[MapPosX()-1][MapPosY()] != MapType::E_None && map[MapPosX()-1][MapPosY()] != MapType::E_Prop))	//ײǽ���ƶ�
				return;

			int YOffset = GetRectCollision().GetYInt() - curFullyMap.GetYInt();

			if (MapPosX() - 1 >= 0 && MapPosY() + 1 < YLENGTH &&
				map[MapPosX() - 1][MapPosY() + 1] != MapType::E_None
				&& YOffset > 0 && YOffset <= DriftOffset && YOffset != 0) //������Ư��
			{
				SetDirection(Role::E_Down);
			}
			else if (MapPosX() - 1 >= 0 && MapPosY() - 1 >= 0 &&
				map[MapPosX() - 1][MapPosY() - 1] != MapType::E_None
				&& -YOffset > 0 && -YOffset <= DriftOffset && YOffset != 0) //������Ư��
			{
				SetDirection(Role::E_Up);
			}

		}
		break;
	}
	case Role::E_Right:
	{
		if (GetRectCollision().GetXInt() == curFullyMap.GetXInt())  //����
		{
			RecoveDirect(); //�����ϴ�Ư��
			if (MapPosX() + 1 >= XLENGTH || (map[MapPosX()+1][MapPosY()] != MapType::E_None && map[MapPosX()+1][MapPosY()] != MapType::E_Prop) != MapType::E_None)	//ײǽ���ƶ�	
			{
				return;
			}

			int YOffset = GetRectCollision().GetYInt() - curFullyMap.GetYInt();

			if (MapPosX() + 1 < XLENGTH && MapPosY() + 1 < YLENGTH &&
				map[MapPosX() + 1][MapPosY() + 1] != MapType::E_None
				&& YOffset > 0 && YOffset <= DriftOffset && YOffset != 0) //������Ư��
			{
				SetDirection(Role::E_Down);
			}
			else if (MapPosX() + 1 < XLENGTH && MapPosY() - 1 >= 0 &&
				map[MapPosX() + 1][MapPosY() - 1] != MapType::E_None
				&& -YOffset > 0 && -YOffset <= DriftOffset && YOffset != 0) //������Ư��
			{
				SetDirection(Role::E_Up);
			}
		}
		break;
	}
	default:
		break;
	}



	//2�������ƶ�
	int move = m_WalkSpeed * deltaTime;

	switch (m_direction)
	{
	case Role::E_Up:
	{
		int newY = GetRectCollision().GetY() + move;
		if (newY > curFullyMap.GetYInt() && curFullyMap.GetYInt() > GetRectCollision().GetYInt()) //�ٽ��������
		{
			SetPixelPosY(GetPixelPosY() + (curFullyMap.GetYInt() - GetRectCollision().GetYInt()));
			Util::DebugOut() << "�������ϣ�" << GetRectCollision().GetXInt();
		}
		else //���ٽ������ƶ�
		{
			SetPixelPosY(GetPixelPosY() + move);
			FullyEnter(false);
		}
		break;
	}
	case Role::E_Down:
	{
		int newY = GetRectCollision().GetY() - move;
		if (newY < curFullyMap.GetYInt() && curFullyMap.GetYInt() < GetRectCollision().GetYInt()) //�ٽ��������
		{
			SetPixelPosY(GetPixelPosY() - (GetRectCollision().GetYInt() - curFullyMap.GetYInt()));
			Util::DebugOut() << "�������ϣ�" << GetRectCollision().GetXInt();
		}
		else //���ٽ������ƶ�
		{
			SetPixelPosY(GetPixelPosY() - move);
			FullyEnter(false);
		}
		break;
	}
	case Role::E_Left:
	{
		int newX = GetRectCollision().GetX() - move;
		if (newX < curFullyMap.GetXInt() && curFullyMap.GetXInt() < GetRectCollision().GetXInt()) //�ٽ��������
		{
			SetPixelPosX(GetPixelPosX() - (GetRectCollision().GetXInt() - curFullyMap.GetXInt()));
			Util::DebugOut() << "�������ϣ�" << GetRectCollision().GetXInt();
		}
		else //���ٽ������ƶ�
		{
			SetPixelPosX(GetPixelPosX() - move);
		}
		break;
	}
	case Role::E_Right:
	{
		int newX = GetRectCollision().GetX() + move;
		if (newX > curFullyMap.GetXInt() && curFullyMap.GetXInt() > GetRectCollision().GetXInt()) //�ٽ��������
		{
			SetPixelPosX(GetPixelPosX() + (curFullyMap.GetXInt() - GetRectCollision().GetXInt()));
			Util::DebugOut() << "�������ϣ�" << GetRectCollision().GetXInt();
		}
		else //���ٽ������ƶ�
		{
			SetPixelPosX(GetPixelPosX() + move);
		}
		break;
	}
	default:
		break;
	}

	UpdateMapPos();
	UpdateRectCollision();

	if (MapPosChanged())
	{
		SetStandOnBubble(false); //��λ�øı䣬�����뿪���ݣ�����Ų�������
		if (map[MapPosX()][MapPosY()] == MapType::E_Prop) //�жϽ����Ƿ��е���
		{
			EatProp(static_cast<Prop*>(currentScene->GetGameObject(MapPosX(), MapPosY())));
		}
	}

}

Role::E_RoleDirection const& Role::GetDirection() const
{
	return m_direction;
}

void Role::SetDirection(E_RoleDirection val)
{
	m_direction = val;
}


bool const& Role::GetCanMove() const
{
	return m_CanMove;
}

void Role::SetCanMove(bool val)
{
	m_CanMove = val;
}

Ability* const& Role::GetAbility() const
{
	return m_pAbility;
}

void Role::SetAbility(Ability* val)
{
	m_pAbility = val;
}

bool Role::HandleMessage(const Telegram& telegram)
{
	return m_pStateMachine->HandleMessage(telegram);
}

Point const& Role::GetStandOnBubble() const
{
	return m_standOnBubble;
}

void Role::SetStandOnBubble(bool val)
{
	if (val == true)
	{
		m_standOnBubble = Point(MapPosX(), MapPosY());
	}
	else
	{
		m_standOnBubble = Point(-1, -1);
	}
}

bool Role::IsStandOnBubble()
{
	if (m_standOnBubble.GetX() != -1
		&& m_standOnBubble.GetY() != -1)
	{
		return false;
	}
	else
		return true;
}

Role::~Role()
{
	delete m_pStateMachine;
	delete m_pAbility;
}

void Role::EatProp(Prop* prop)
{
	switch (prop->GetPropType())
	{
	case Prop::E_Popo:
	{
		m_pAbility->Crease(Ability::E_BubbleNum);
		break;
	}
	case Prop::E_Power:
	{
		m_pAbility->Crease(Ability::E_Power);
		break;
	}
	case Prop::E_Run1:
	{
		m_pAbility->Crease(Ability::E_RunSpeed);
		break;
	}
	default:
	{
		return;
	}
	}

	PlayScene* currentScene = static_cast<PlayScene*>(g_pLGCenter->GetCurrentScene());
	currentScene->DeleteObject(prop->GetObjID());
	currentScene->ChangeMap(MapPosX(), MapPosY(), MapType::E_None);
}

void Role::StopWalk()
{
	switch (this->GetDirection())
	{
	case E_RoleDirection::E_Up:
	{
		this->GetStateMachine()->ChangeState(IdleUp::Instance());
		break;
	}
	case E_RoleDirection::E_Down:
	{
		this->GetStateMachine()->ChangeState(IdleDown::Instance());
		break;
	}
	case E_RoleDirection::E_Left:
	{
		this->GetStateMachine()->ChangeState(IdleLeft::Instance());
		break;
	}
	case E_RoleDirection::E_Right:
	{
		this->GetStateMachine()->ChangeState(IdleRight::Instance());
		break;
	}
	}
}

bool Role::IsFullyArrive()
{
	return (GetPixelPosY() == Util::ORIGINPIX.GetX() + MapPosX()*Util::MAPPIECEPIX
		&& GetPixelPosY() == Util::ORIGINPIX.GetY() + MapPosY()*Util::MAPPIECEPIX);
}

void Role::FullyArrive()
{
	switch (this->GetDirection())
	{
	case E_RoleDirection::E_Up:
	{
		SetDirection(Role::E_Left);
		return;
	}
	case E_RoleDirection::E_Down:
	{
		SetPixelPosY(Util::ORIGINPIX.GetY() + MapPosY()*Util::MAPPIECEPIX);
		break;
	}
	case E_RoleDirection::E_Left:
	{
		SetPixelPosX(Util::ORIGINPIX.GetX() + MapPosX()*Util::MAPPIECEPIX);
		break;
	}
	case E_RoleDirection::E_Right:
	{
		SetPixelPosX(Util::ORIGINPIX.GetX() + MapPosX()*Util::MAPPIECEPIX);
		break;
	}
	}
}

void Role::UpdateRectCollision(int offsetX /*= 0*/, int offsetY /*= 0*/, int offsetWidth /*= 0*/, int offsetHeight /*= 0*/)
{
	Object::UpdateRectCollision(4, 2, -8, -24); //���ý�ɫ��ײ��
}

void Role::RecoveDirect()
{
	wstring roleState = static_cast<RoleState*>(GetStateMachine()->GetCurrentState())->GetRoleStateName();

	if (roleState == L"WalkUp")
	{
		SetDirection(Role::E_Up);
	}
	else if (roleState == L"WalkDown")
	{
		SetDirection(Role::E_Down);
	}
	else if (roleState == L"WalkLeft")
	{
		SetDirection(Role::E_Left);
	}
	else if (roleState == L"WalkRight")
	{
		SetDirection(Role::E_Right);
	}

}

