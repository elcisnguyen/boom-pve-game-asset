#pragma once
#include <iostream>
#include <math.h>
#include "LGHead.h"

struct Telegram
{
	int          Sender;
	int          Receiver;
	int          Msg;
	double       DispatchTime;	// ������Ϣ���͵�ʱ�䣬����������Ҳ�����Ƕ��֮���ʱ���
	void*        ExtraInfo;		// �������Ϣ

	Telegram():DispatchTime(-1),
		Sender(-1),
		Receiver(-1),
		Msg(-1)
	{}

	Telegram(double time,
		int    sender,
		int    receiver,
		int    msg,
		void*  info = NULL): DispatchTime(time),
		Sender(sender),
		Receiver(receiver),
		Msg(msg),
		ExtraInfo(info)
	{}

};

// �籨�����������ȶ��У������رȽϲ�����ʱ�����ֵ�����˵籨��ʱ���Ƿ�����ȵ�
const double SmallestDelay = 0.25;

inline bool operator==(const Telegram& t1, const Telegram& t2)
{
	return ( fabs(t1.DispatchTime-t2.DispatchTime) < SmallestDelay) &&
		(t1.Sender == t2.Sender)        &&
		(t1.Receiver == t2.Receiver)    &&
		(t1.Msg == t2.Msg);
}

inline bool operator<(const Telegram& t1, const Telegram& t2)
{
	if (t1 == t2)
	{
		return false;
	}
	else
	{
		return  (t1.DispatchTime < t2.DispatchTime);
	}
}