#pragma once

#include "Shader.h"
#include <map>

class VertexShader : public Shader
{
public:
	VertexShader(ID3D11Device* pID3DDevice);
	~VertexShader();
	void CreateVS(std::wstring vsName, std::wstring filePath);	//����������ɫ��
	ID3D11VertexShader* const& GetVS(std::wstring vsName) const;	//���ӳ��õ��Ķ�����ɫ��
	ID3DBlob* const& GetBlob(std::wstring vsName) const;			//���ӳ��õ��Ķ�����ɫ�����뻺��
	void DelBlob(std::wstring vsName);			//���ӳ��õ��Ķ�����ɫ�����뻺��
private:
	std::map<std::wstring,ID3D11VertexShader*> m_MapVS;	//�ļ�·���Ͷ�����ɫ����
	std::map<std::wstring,ID3DBlob*> m_MapBlob;			//�ļ�·���ͱ���Ķ�����ɫ�������
};