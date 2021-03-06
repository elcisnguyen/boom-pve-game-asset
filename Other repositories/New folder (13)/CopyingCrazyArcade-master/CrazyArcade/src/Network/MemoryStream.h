#pragma once
#include <cstdlib>
#include <cstdint>
#include <type_traits>
#include <iostream>
#include <algorithm>
#include <vector>
#include  "ByteSwap.h"
#define STREAM_ENDIANNESS 0
#define PLATFORM_ENDIANNESS 0

class GameObject;
class LinkingContext;

class OutputMemoryStream
{
public:
	OutputMemoryStream() :
		mBuffer(nullptr), mHead(0), mCapacity(0)
	{
		ReallocBuffer(32);
	}

	~OutputMemoryStream() { std::free(mBuffer); }

	//get a pointer to the data in the stream
	const char*	GetBufferPtr()	const { return mBuffer; }
	uint64_t	GetLength()		const { return mHead; }

	void		Write(const void* inData,size_t inByteCount);

	template< typename T > void Write(T inData)
	{
		static_assert(std::is_arithmetic< T >::value ||
			std::is_enum< T >::value,
			"Generic Write only supports primitive data types");

		if (STREAM_ENDIANNESS == PLATFORM_ENDIANNESS)
		{
			Write(&inData, sizeof(inData));
		}
		else
		{
			T swappedData = ByteSwap(inData);
			Write(&swappedData, sizeof(swappedData));
		}

	}

	void Write(const std::vector< int >& inIntVector)
	{
		size_t elementCount = inIntVector.size();
		Write(elementCount);
		Write(inIntVector.data(), elementCount * sizeof(int));
	}

	template< typename T >
	void WriteVector(const std::vector< T >& inVector)
	{
		for (auto element : inVector)
		{
			element.Write(*this);
		}
	}

	void Write(const std::string& inString)
	{
		size_t elementCount = inString.size();
		Write(elementCount);
		Write(inString.data(), elementCount * sizeof(char));
	}

private:
	void		ReallocBuffer(uint64_t inNewLength);

	char*		mBuffer;
	uint64_t	mHead;
	uint64_t	mCapacity;

	LinkingContext* mLinkingContext;
};

class InputMemoryStream
{
public:
	InputMemoryStream(char* inBuffer, uint64_t inByteCount) :
		mBuffer(inBuffer), mCapacity(inByteCount), mHead(0),
		mLinkingContext(nullptr) {}

	~InputMemoryStream() { std::free(mBuffer); }

	uint64_t GetRemainingDataSize() const
	{
		return mCapacity - mHead;
	}

	void		Read(void* outData, uint64_t inByteCount);


	template< typename T > void Read(T& outData)
	{
		static_assert(std::is_arithmetic< T >::value ||
			std::is_enum< T >::value,
			"Generic Read only supports primitive data types");
		Read((void*)(&outData), sizeof(outData));
	}

	template< typename T >
	void ReadVector(std::vector< T >& outVector, size_t elementCount)
	{
		outVector.resize(elementCount);
		for (auto& element : outVector)
		{
			element.Read(*this);
		}
	}

	//void Read(GameObject*& outGameObject)
	//{
	//	uint32_t networkId;
	//	Read(networkId);
	//	outGameObject = mLinkingContext->GetGameObject(networkId);
	//}

private:
	char*		mBuffer;
	uint64_t	mHead;
	uint64_t	mCapacity;

	LinkingContext* mLinkingContext;
};

