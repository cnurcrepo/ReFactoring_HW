using System;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;

/// <summary>
/// 주의 : 이 클래스는 Monobehavior를 상속받지 않아야 함
/// 그래서 Adapter 클래스를 따로 만들었다.
/// </summary>

namespace UnityRPG_UnitTest
{
    [Serializable]
    public class MonsterDropItem
    {
        public int ItemID;
        public float DropProb;
        public int DropMinNumber;
        public int DropMaxNumber;
        public MonsterDropItem(int _ItemID, float _DropProb, int _DropMinNumber, int _DropMaxNumber)
        {
            ItemID = _ItemID;
            DropProb = _DropProb;
            DropMinNumber = _DropMinNumber;
            DropMaxNumber = _DropMaxNumber;
        }

    }
}