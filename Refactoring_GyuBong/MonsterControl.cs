using System;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.AI;

// 본 파일 코드의 길이가 길어, 리팩토링 관련 부분인 ItemDrop 쪽 메서드만 남겨두었습니다.
namespace UnityChanRPG
{
    public class MonsterControl : MonoBehaviour
    {
        // 이 몬스터의 드롭 아이템 중 확률로 드롭할 아이템의 ID를 결정하고, ItemPool을 거쳐 드롭함
        // 1개 이상 드롭되는 아이템의 경우 드롭될 갯수 역시 확률로 결정
        private void ItemDrop()
        {
            float itemProb = decideRandomSpawnItem();

            float[] diffProbs = calcDiffWithItemProb(monsterAdpt.monster, itemProb);

            int Index = returnMinIntevalIndex(diffProbs);

            if (isNotSpawn(itemProb, diffProbs))
            {
                return;
            }

            if (isSingleItem(Index))
            {
                ItemPool.Instance.DropItem(
                    ID: monsterAdpt.monster.monsterDropItems[Index].ItemID, 
                    spawnPosition: transform.position + controller.center);
            }

            else
            {
                ItemPool.Instance.DropItem(
                    ID: monsterAdpt.monster.monsterDropItems[Index].ItemID,
                    spawnPosition: transform.position + controller.center,
                    generateValues: decideRandomItemValues(Index));
            }

        }

        private float[] calcDiffWithItemProb(Monster monster, float itemProb)
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
        private bool isNotSpawn(float itemProb, float[] minProb)
        {
            return itemProb > monsterAdpt.monster.dropItemProbAccum[minProb.Length - 1];
        }

        private bool isSingleItem(int index)
        {
            return monsterAdpt.monster.monsterDropItems[index].DropMaxNumber == 1;
        }

        private int returnMinIntevalIndex(float[] minProb)
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

        private int decideRandomItemValues(int index)
        {
            return UnityEngine.Random.Range(monsterAdpt.monster.monsterDropItems[index].DropMinNumber, monsterAdpt.monster.monsterDropItems[index].DropMaxNumber + 1);
        }

        private float decideRandomSpawnItem()
        {
            return ((float)UnityEngine.Random.Range(0, 10000)) / 10000f;
        }

    }
}