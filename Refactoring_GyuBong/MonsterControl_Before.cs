using System;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.AI;

namespace UnityChanRPG
{
    public class MonsterControl_Before : MonoBehaviour
    {
       // 이 몬스터의 드롭 아이템 중 확률로 드롭할 아이템의 ID를 결정하고, ItemPool을 거쳐 드롭함
        // 1개 이상 드롭되는 아이템의 경우 드롭될 갯수 역시 확률로 결정
        private void ItemDrop()
        {
            float probability = ((float)UnityEngine.Random.Range(0, 10000)) / 10000f;

            float[] minProb = new float[monsterAdpt.monster.monsterDropItems.Count];

            for (int i = 1; i < monsterAdpt.monster.dropItemProbAccum.Length; i++)
            {
                minProb[i - 1] =

                    Math.Abs(monsterAdpt.monster.dropItemProbAccum[i] - probability - 0.0001f) < Math.Abs(monsterAdpt.monster.dropItemProbAccum[i - 1] - probability) ?

                    Math.Abs(monsterAdpt.monster.dropItemProbAccum[i] - probability - 0.0001f) : Math.Abs(monsterAdpt.monster.dropItemProbAccum[i - 1] - probability);
            }

            int Index = 0;
            float result = 1.0f;

            for (int i = 0; i < minProb.Length; i++)
            {
                if (result > minProb[i])
                {
                    result = minProb[i];
                    Index = i;
                }
            }

            if (probability > monsterAdpt.monster.dropItemProbAccum[minProb.Length])
            {
                return;
            }

            if (monsterAdpt.monster.monsterDropItems[Index].DropMaxNumber == 1)
            {
                ItemPool.Instance.DropItem(monsterAdpt.monster.monsterDropItems[Index].ItemID, transform.position + controller.center);
            }

            else
            {
                ItemPool.Instance.DropItem(
                    monsterAdpt.monster.monsterDropItems[Index].ItemID,
                    transform.position + controller.center,
                    UnityEngine.Random.Range(monsterAdpt.monster.monsterDropItems[Index].DropMinNumber, monsterAdpt.monster.monsterDropItems[Index].DropMaxNumber + 1));
            }


        }


    }
}