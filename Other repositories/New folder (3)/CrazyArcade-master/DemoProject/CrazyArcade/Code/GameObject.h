#pragma once
#include "Object.h"

class GameObject : public Object
{
public:
	GameObject();
	GameObject(int mapPosX, int mapPoxY);
	Point CalPixelPos(int mapPosX, int mapPosY);
	Point CalMapPos(Point pixelPos);
	void UpdateMapPos();
	int const& MapPosX() const;
	void MapPosX(int val);
	int const& MapPosY() const;
	void MapPosY(int val);
	bool MapPosChanged() const { return m_mapPosChanged; }
	void MapPosChanged(bool val) { m_mapPosChanged = val; }
private:
	//ԭ�������½�
	int m_mapPosX;
	int m_mapPosY;
	int m_LastMapPosX;
	int m_LastMapPosY;
	bool m_mapPosChanged;
};

