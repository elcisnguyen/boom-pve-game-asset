#pragma once

#include "DXHead.h"

class ViewPort
{
public:
	ViewPort();
	~ViewPort();
	void CreateVP(std::wstring vpName,int width,int height);			//�����ӿ�
	D3D11_VIEWPORT* const& GetVP(std::wstring vpName) const;	//���ӳ��õ����ӿ�
private:
	ID3D11Device* m_pID3DDevice;
	std::map<std::wstring,D3D11_VIEWPORT*> m_MapVP;	
};