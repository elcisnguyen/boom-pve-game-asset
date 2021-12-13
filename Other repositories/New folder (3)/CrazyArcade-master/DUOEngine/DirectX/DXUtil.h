#pragma once

#include <map>
#include "Util.h"

#define SAFE_RELEASE(point) if (point) {point->Release(); point = 0;}


namespace Util
{
	//�ͷ�Map��COM����ֵ
	template<typename T>
	void MapSecondRelease(T &RelMap)
	{
		for(T::iterator itr = RelMap.begin(); itr != RelMap.end(); itr++)
		{
			SAFE_RELEASE(itr->second);
		}
	}



	
}