#include "RoleState.h"
#include "LGInput.h"

//------------------------------------------------------------------------RoleState�෽��ʵ��

bool RoleState::OnMessage(Role* agent, const Telegram& msg)
{
	return false;
}

std::wstring const& RoleState::GetRoleStateName() const
{
	return m_RoleStateName;
}

void RoleState::PreprocessState(Role* role, RoleState* roleState)
{
	roleState->Enter(role);
	role->GetStateMachine()->SetPreviousState(roleState);
}

//------------------------------------------------------------------------Idle�෽��ʵ��

void Idle::Execute(Role* role, float deltatime )
{

}

void Idle::Exit(Role* role)
{

}


//------------------------------------------------------------------------IdleUp�෽��ʵ��

void IdleUp::Enter(Role* role)
{
	role->HavaCurrentSprite().SetRowAndCol(0,0);
	role->SetDirection(Role::E_Up);
	m_RoleStateName = L"IdleUp";
}

//------------------------------------------------------------------------IdleDown�෽��ʵ��


void IdleDown::Enter(Role* role)
{
	role->HavaCurrentSprite().SetRowAndCol(1,0);
	role->SetDirection(Role::E_Down);
	m_RoleStateName = L"IdleDown";
}

//------------------------------------------------------------------------IdleLeft�෽��ʵ��



void IdleLeft::Enter(Role* role)
{
	role->HavaCurrentSprite().SetRowAndCol(2,0);
	role->SetDirection(Role::E_Left);
	m_RoleStateName = L"IdleLeft";
}

//------------------------------------------------------------------------IdleRight�෽��ʵ��


void IdleRight::Enter(Role* role)
{
	role->HavaCurrentSprite().SetRowAndCol(3,0);
	role->SetDirection(Role::E_Right);
	m_RoleStateName = L"IdleRight";
}

//------------------------------------------------------------------------Walk�෽��ʵ��

void Walk::Execute(Role* role, float deltatime )
{
	role->UpdateAnimateFrame(deltatime);
	role->Move(deltatime);
}

void Walk::Exit(Role* role)
{

}

//------------------------------------------------------------------------WalkUp�෽��ʵ��


void WalkUp::Enter(Role* role)
{
	PreprocessState(role,IdleUp::Instance());
	m_RoleStateName = L"WalkUp";
}



//------------------------------------------------------------------------WalkDown�෽��ʵ��


void WalkDown::Enter(Role* role)
{
	PreprocessState(role,IdleDown::Instance());
	m_RoleStateName = L"WalkDown";
}



//------------------------------------------------------------------------WalkLeft�෽��ʵ��


void WalkLeft::Enter(Role* role )
{
	PreprocessState(role,IdleLeft::Instance());
	m_RoleStateName = L"WalkLeft";
}



//------------------------------------------------------------------------WalkRight�෽��ʵ��


void WalkRight::Enter(Role* role )
{
	PreprocessState(role,IdleRight::Instance());
	m_RoleStateName = L"WalkRight";
}


