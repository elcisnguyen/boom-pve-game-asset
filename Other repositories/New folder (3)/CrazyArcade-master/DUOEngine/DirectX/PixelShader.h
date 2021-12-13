#pragma once

#include "Shader.h"


class PixelShader : public Shader
{
public:
	PixelShader(ID3D11Device* pID3DDevice);
	~PixelShader();
	void CreatePS(std::wstring psName, std::wstring filePath);		//����������ɫ��
	ID3D11PixelShader* const& GetPS(std::wstring psName) const;	//���ӳ��õ���������ɫ��
private:
	std::map<std::wstring,ID3D11PixelShader*> m_MapPS;	//�ļ�·����������ɫ����
};