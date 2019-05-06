using System;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;

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
