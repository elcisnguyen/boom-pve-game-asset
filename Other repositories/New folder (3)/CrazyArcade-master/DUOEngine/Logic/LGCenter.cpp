#include "LGCenter.h"
#include "PlayScene.h"

LGCenter::LGCenter() : m_Deltatime(0)
{
	// �������������
	srand(GetTickCount());
	// ����״̬��
	SetStateMachine(new StateMachine<LGCenter>(this));
}

LGCenter::~LGCenter()
{
	SAFE_DELETE(m_pStateMachine);
}

LGCenter* LGCenter::Instance()
{
	static LGCenter* instance = new LGCenter;
	return instance;
}

Scene* LGCenter::GetCurrentScene()
{
	return static_cast<Scene*>(GetStateMachine()->GetCurrentState());
}

void LGCenter::SetCurrentScene(Scene* currentScene)
{
	m_pStateMachine->SetCurrentState(currentScene);
}

void LGCenter::Update()
{
	// ����FPS
	GetFPS();
	// ����״̬������ĳ���
	m_pStateMachine->Update(m_Deltatime);
	// ����籨��Ϣ
	g_pMessageMachine->Update();
}

StateMachine<LGCenter>* const& LGCenter::GetStateMachine() const
{
	return m_pStateMachine;
}

void LGCenter::SetStateMachine(StateMachine<LGCenter>* val)
{
	m_pStateMachine = val;
}

float LGCenter::GetFPS()
{
	//�����ĸ���̬����
	static float  fps = 0; //������Ҫ�����FPSֵ
	static int    frameCount = 0;//֡��
	static float  currentTime =0.0f;//��ǰʱ��

	frameCount++;//ÿ����һ��GetFPS()������֡������1
	currentTime = timeGetTime()*0.001f;//��ȡϵͳʱ�䣬����timeGetTime�������ص����Ժ���Ϊ��λ��ϵͳʱ�䣬������Ҫ����0.001���õ���λΪ���ʱ��

	static float  lastTime = currentTime;//����ʱ��

	//�����ǰʱ���ȥ����ʱ�������1���ӣ��ͽ���һ��FPS�ļ���ͳ���ʱ��ĸ��£�����֡��ֵ����
	if(currentTime - lastTime > 1.0f) //��ʱ�������1����
	{
		fps = (float)frameCount /(currentTime - lastTime);//������1���ӵ�FPSֵ

		m_Deltatime = 1 / fps;
		lastTime = currentTime; //����ǰʱ��currentTime��������ʱ��lastTime����Ϊ��һ��Ļ�׼ʱ��

		frameCount    = 0;//������֡��frameCountֵ����
	}

	return fps;
}
