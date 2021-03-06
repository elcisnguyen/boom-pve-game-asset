#include"Object/MyMap.h"
#include"Network/Msg.h"
#include"Object/Prop.h"
USING_NS_CC;

MyMap::MyMap()
{
}

MyMap::~MyMap()
{
}


bool MyMap::init()
{
	assert(getLayer("nomove") != nullptr);
	m_nomoveLayer = getLayer("nomove");
	assert(getLayer("remove") != nullptr);
	m_removeLayer = getLayer("remove");
	assert(getLayer("building") != nullptr);
	m_buildingLayer = getLayer("building");
	assert(getLayer("background") != nullptr);
	m_backgroundLayer = getLayer("background");
	assert(getLayer("building top") != nullptr);
	m_building_topLayer = getLayer("building top");
	assert(getLayer("building top top") != nullptr);
	m_building_top_topLayer = getLayer("building top top");

	m_building_top_topLayer->setZOrder(12);
	m_building_topLayer->setZOrder(11);
	m_buildingLayer->setZOrder(10);
	m_backgroundLayer->setZOrder(-1);
	auto Size = getMapSize();
	for (int i = 0; i <= Size.width; ++i)
	{
		for (int j = 0; j <= Size.height; ++j)
		{
			m_gid_nomove = m_nomoveLayer->getTileGIDAt(Vec2(i, j));
			if (m_gid_nomove > 0)break;
		}
		if (m_gid_nomove > 0)break;
	}
	for (int i = 0; i <= Size.width; ++i)
	{
		for (int j = 0; j <= Size.height; ++j)
		{
			m_gid_remove = m_removeLayer->getTileGIDAt(Vec2(i, j));
			if (m_gid_remove > 0)break;
		}
		if (m_gid_remove > 0)break;
	}

	return true;
}


Vec2 MyMap::positionToTileCoord(Vec2 pos)  //??Ļ????ת??Ƭ????
{
	int x = pos.x / getTileSize().width;
	int y = ((getMapSize().height * getTileSize().height) - pos.y) /
		getTileSize().height;
	return Vec2(x, y);

}

Vec2 MyMap::tileCoordToPosition(Vec2 pos)  //??Ƭ????ת??Ļ????
{
	int x = (pos.x + 0.5) * getTileSize().width;
	int y = (getMapSize().height - pos.y - 0.5) * getTileSize().height;
	return Vec2(x, y);
}

bool MyMap::ifCanMove(Vec2 pos)    //?жϿɷ??ƶ?
{
	Vec2 tileCoord = positionToTileCoord(pos);

	//can't move because it can't go out the screen
	if (!ifInScene(pos))return false;

	//can't move because there are something 
	if (m_nomoveLayer->getTileGIDAt(tileCoord) > 0) return false;

	return true;
}

void MyMap::setOrder(Scene*scene)
{
	m_backgroundLayer->removeFromParent();
	scene->addChild(m_backgroundLayer, -1);
}

void MyMap::setNomove(Vec2 pos)
{
	auto tileCoord = positionToTileCoord(pos);
	m_nomoveLayer->setTileGID(m_gid_nomove, tileCoord);
}

void MyMap::removeNomove(Vec2 pos)
{
	auto tileCoord = positionToTileCoord(pos);
	if (m_nomoveLayer->getTileGIDAt(tileCoord) == 0) return;
	m_nomoveLayer->removeTileAt(tileCoord);
}

void MyMap::damage(Vec2 pos)
{
	auto tileCoord = positionToTileCoord(pos);
	assert(ifCanDamage(pos));
	if (!ifCanDamage(pos)) return;

	removeNomove(pos);
	m_removeLayer->removeTileAt(tileCoord);
	m_buildingLayer->removeTileAt(tileCoord);

	pos.y += getTileSize().height;
	if (!ifInScene(pos)) return;

	tileCoord=positionToTileCoord(pos);
	m_building_topLayer->removeTileAt(tileCoord);

	auto tile = positionToTileCoord(pos);
	int propType = RandProp();
	if(propType!=-1) SendMsg_CreateProp(tile.x, tile.y,propType);
	Sleep(10);
}

int MyMap::RandProp()
{
	const int item_breed = Prop::Type::MaxNum; //????????
	static const int item_breedMaxNumber[item_breed] = { 10,10,10,5,3 }; //????ÿ?ֵ??????ܴ??ڵ???????Ŀ
	static int item_matrix[item_breed] = { 0,0,0,0,0 };	//?Ѿ??????˵ĵ???????

	static int breed=(breed+77) % 101;
	srand((unsigned)time(NULL));
	int n = (rand()+150) % 100;
	if (n < 30) return -1;       //??????????

	int m = (n+breed) % item_breed;

	int change = 0;  //?Ѿ????????ĵ?????????
	while (true)
	{
		if (change >= item_breed) break;

		if (item_matrix[m] == item_breedMaxNumber[m])	++change;

		if (item_matrix[m] < item_breedMaxNumber[m])
		{
			++item_matrix[m];
			return m;
		}
		else
		{
			m = (m + 1) % item_breed;
		}
	}
	return -1;
}

bool MyMap::ifCanDamage(Vec2 pos)
{
	auto tileCoord = positionToTileCoord(pos);

	if (!ifInScene(pos)) return false;

	if (m_removeLayer->getTileGIDAt(tileCoord) == 0) return false;

	return true;
}

bool MyMap::ifInScene(Vec2 pos)
{
	auto Size = getMapSize();
	Size.width *= getTileSize().width;
	Size.height *= getTileSize().height;
	if (pos.x<0 || pos.y<0 || pos.x>Size.width || pos.y>Size.height)
		return false;
	return true;
}