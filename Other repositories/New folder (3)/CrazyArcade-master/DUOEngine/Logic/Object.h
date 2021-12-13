#pragma once

#include "Sprite.h"
#include "Telegram.h"
#include "Rect.h"

class Object
{
public:
	Object(std::wstring objName, Sprite currentSprite,int layout, Point pixelPos, 
		int objType = 0);
	Object();
	virtual ~Object();

	void LoadMe(Point picSize);

	Sprite const& GetCurrentSprite() const;
	int const& GetLayout() const;
	std::wstring const& GetObjName() const;
	int GetPixelPosX() const;
	int GetPixelPosY() const;
	int const& GetObjID() const;
	bool const& GetVisiable() const;
	LGRect GetSpriteRect();
	LGRect const& GetRectCollision() const;
	int const& GetObjectType() const;

	void SetCurrentSprite(IN Sprite val);
	void SetLayout(int val);
	void SetObjName(std::wstring val);
	void SetPixelPosX(float val);
	void SetPixelPosY(float val);
	void SetVisiable(bool val);
	void SetObjectType(int val);
	Sprite& HavaCurrentSprite();

	virtual bool HandleMessage(const Telegram& telegram);

	bool OrderCompare(Object* const& rhs);

	
	virtual void Update(float deltaTime);
	virtual bool UpdateAnimateFrame(float deltaTime, const int* frame = NULL, int frameCount = 0);	//������ѭ��һ�飬����true
	virtual void UpdateRectCollision(int offsetX = 0, int offsetY = 0, int offsetWidth = 0, int offsetHeight = 0);
	void UpdateSpriteRect();

	//-------------------------------������㷨-begin------------------------------

	void SpreadDirty(); //������Դ����Ⱦ��
	void ReceiveDirty(LGRect val);//���վֲ�����β�λ������Ⱦ��
	void clearDirty(); //��Ⱦ��Ͽ�����������
	std::vector<LGRect> const& GetRectDirty() const; //ȡ�øö�����ľֲ����β�λ

	//-------------------------------������㷨-end------------------------------
protected:
	Sprite m_currentSprite;
	int m_Layout;
	std::wstring m_objName;
	Point m_pixelPos;
	LGRect m_RectCollision;
	int m_ObjectType;
	static int NextID;
	int m_ID;
	std::vector<LGRect> m_RectDirty;
	LGRect m_SpriteRect;
	bool m_visiable;
private:
	float m_addToOneSum;
	int m_frameIndex;
	bool m_hasInit;
};