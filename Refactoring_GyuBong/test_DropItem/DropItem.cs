using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using UnityEngine;

namespace UnityRPG_UnitTest
{
    class DropItem
    {
        public MonsterAdapter adpt;

        public DropItem(MonsterAdapter _apdt) {
            this.adpt = _apdt;
        }

        public void ItemDrop()
        {
            float itemProb = decideRandomSpawnItem();

            float[] diffProbs = calcDiffWithItemProb(adpt.monster, itemProb);

            int Index = returnMinIntevalIndex(diffProbs);

            if (isNotSpawn(itemProb, diffProbs.Length))
            {
                return;
            }

            if (isSingleItem(Index))
            {
                // Action
            }

            else
            {
                // Action
            }

        }

        public float[] calcDiffWithItemProb(Monster monster, float itemProb)
        {

            float[] diffWithItemProb = new float[monster.monsterDropItems.Count];

            for (int i = 1; i < monster.dropItemProbAccum.Length; i++)
            {
                diffWithItemProb[i - 1] =

                    Math.Abs(monster.dropItemProbAccum[i] - itemProb - 0.0001f) < Math.Abs(monster.dropItemProbAccum[i - 1] - itemProb) ?

                    Math.Abs(monster.dropItemProbAccum[i] - itemProb - 0.0001f) : Math.Abs(monster.dropItemProbAccum[i - 1] - itemProb);

            }

            return diffWithItemProb;
        }

        // itemProb이 dropItemProbAccum의 마지막 원소보다 큰 값이 나온다면 드롭되지 않는다. 
        public bool isNotSpawn(float itemProb, int length)
        {
            return itemProb > adpt.monster.dropItemProbAccum[length - 1];
        }

        public bool isSingleItem(int index)
        {
            return adpt.monster.monsterDropItems[index].DropMaxNumber == 1;
        }

        public int returnMinIntevalIndex(float[] minProb)
        {
            int index = -1;
            float result = 1.0f;

            for (int i = 0; i < minProb.Length; i++)
            {
                if (result > minProb[i])
                {
                    result = minProb[i];
                    index = i;
                }
            }
            return index;
        }

        public int decideRandomItemValues(int index)
        {
            return UnityEngine.Random.Range(adpt.monster.monsterDropItems[index].DropMinNumber, adpt.monster.monsterDropItems[index].DropMaxNumber + 1);
        }

        public float decideRandomSpawnItem()
        {
            // both inclu
            return ((float)UnityEngine.Random.Range(0, 10000)) / 10000f;
        }

    }
}
