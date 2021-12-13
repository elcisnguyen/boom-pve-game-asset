#pragma once
#pragma comment(lib, "winmm.lib")

#include <windows.h>
#include "Util.h"

#define Clock CrudeTimer::Instance()

class CrudeTimer
{
public:
	CrudeTimer();
	~CrudeTimer();
	// ���شӸ���ʵ����������ʱ�䣨�룩
	double GetTimePass();
	void reset();
private:
	// �����౻ʵ������ʱ���¼��ʼ��ʱ��
	double m_dStartTime;
};