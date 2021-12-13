#include "MessageDispatcher.h"
#include "CrudeTimer.h"
#include "Object.h"
#include "LGCenter.h"

#include <iostream>

using std::set;

MessageDispatcher::MessageDispatcher()
{
	m_GameTimer = new CrudeTimer;
}

MessageDispatcher::~MessageDispatcher()
{
	delete m_GameTimer;
}

void MessageDispatcher::SendMessage(Object* pReceiver,
	const Telegram& telegram)
{
	if (!pReceiver->HandleMessage(telegram))
	{
		//��Ϣ�޷�������
		OutputDebugStringW(L"Message not handled");
	}
}

void MessageDispatcher::MakeMessage(double delay,
	int sender,
	int receiver,
	int msg,
	void* ExtraInfo)
{
	Object* pSender   = LGCenter::Instance()->GetCurrentScene()->HavaObject(sender);
	Object* pReceiver = LGCenter::Instance()->GetCurrentScene()->HavaObject(receiver);

	//�����籨
	Telegram telegram(0, sender, receiver, msg, ExtraInfo);

	//���û����ʱ������������
	if (delay <= 0.0f)                                                        
	{
		if(pReceiver)
			SendMessage(pReceiver, telegram);
	}
	else //�������籨��ʵ�ʷ���ʱ���
	{
		double CurrentTime = m_GameTimer->GetTimePass(); 

		telegram.DispatchTime = CurrentTime + delay;

		// Ȼ��Ž�������
		m_MsgPriorityQueue.insert(telegram);   

	}
}


void MessageDispatcher::Update()
{
	double CurrentTime = m_GameTimer->GetTimePass();

	while( !m_MsgPriorityQueue.empty() &&
		(m_MsgPriorityQueue.begin()->DispatchTime < CurrentTime) )
	{
		const Telegram& telegram = *m_MsgPriorityQueue.begin();

		Object* pReceiver = LGCenter::Instance()->GetCurrentScene()->HavaObject(telegram.Receiver);

		if (pReceiver != NULL)
		{
			SendMessage(pReceiver, telegram);
		}

		m_MsgPriorityQueue.erase(m_MsgPriorityQueue.begin());
	}
}



