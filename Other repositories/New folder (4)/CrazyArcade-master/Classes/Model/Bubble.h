#ifndef __MODEL_BUBBLE_H__
#define __MODEL_BUBBLE_H__

#include "Entity.h"

class Bubble : public Entity
{
public:

    enum class Status : std::int8_t
    {
        ALIVE,
        BOOM
    };

    static Bubble * create(const std::string & id, const std::string& playerID, uint8_t damage = 1);

    bool init();

    uint8_t getDamage();

    std::string getPlayerID();

    void setStatus(Status status);
private:
    uint8_t _damage;
    std::string _playerID;

    void initAnimation();
};

class BubbleWave : public Entity {
public:
    enum PosInWave {
        CENTER,
        MIDDLE,
        TERMINAL
    };

    enum Direction {
        LEFT,
        RIGHT,
        UP,
        DOWN,
        NONE
    };

    bool init(PosInWave pos, Direction direction); 

    static BubbleWave* create(PosInWave pos, Direction direction);

private:

    void getExplosionString(PosInWave pos, Direction direction);
    
    std::string animationName;
};
#endif
