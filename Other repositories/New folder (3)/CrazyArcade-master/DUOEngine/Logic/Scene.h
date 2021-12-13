#pragma once

#include "LGHead.h"
#include "State.h"

class LGCenter;
class Object;

class Scene : public State<LGCenter>
{
public:
	Scene();
	virtual ~Scene();
	
	// Get(Hava)����
	std::vector<Object*> const& GetAllObject();
	std::vector<Object*> const& GetUnLoadObject() const;
	std::vector<Object*> const& GetAllDirtyObject();
	bool const& GetGameEnd() const;
	std::vector<Object*>& HaveAllObject();
	Object* HavaObject(std::wstring objName);
	Object* HavaObject(int objID);

	// Set����
	void SetGameEnd(bool val);

	// �����ɾ��
	void InsertObject(Object* pInsertObj);
	bool DeleteObject(std::wstring objName);
	bool DeleteObject(int objID);

	//����
	void SortObject(std::vector<Object*>* objVec); //�Զ�����а����y������
	void hadLoadAll(); //�Ѿ���������δ����Ķ���

	//-------------------------------������㷨-begin------------------------------

	//������㷨���Ĳ���
	//1.������Ⱦ�����еľ���
	void DirtyScene(LGRect dirtyRect);
	//2.��������Ⱦ�ؽӴ���Ⱦλ�õ����ж���
	void DirtyObject();
	//3.�����Ⱦ����
	void InsertDirtyObject(Object* obj); //�����ǳ��������������Ⱦ���ټ���
	//4.������
	void ClearDirty();

	//-------------------------------������㷨-end------------------------------
private:
	std::vector<LGRect> m_DitryRectVec;		//�����е������������Ⱦ����

	// �����еĶ����Ϊ����������
	std::vector<Object*> m_allObject;		//ȫ���������ڲ�����Ϣ
	std::vector<Object*> m_UnLoadObject;	//δ���������������ʱ��ʼ������
	std::vector<Object*> m_allDirtyObject;	//�����������Ⱦ

	bool m_gameEnd;	//������Ϸ����
};

