using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEditor;
using NUnit.Framework;

/// <summary>
/// DropItem 및 내부 메서드들을 테스트 하는 테스트 클래스
/// </summary>

namespace UnityRPG_UnitTest
{
    [TestFixture]
    public class DropItemTest : MonoBehaviour
    {
        DropItem dropItem;
        Monster monster;
        MonsterAdapter adpt;

        [SetUp]
        public void test_Env_SetUp()
        {
            monster = new Monster();

            monster.monsterDropItems.Add(new MonsterDropItem(10001, 0.25f, 1, 3));
            monster.monsterDropItems.Add(new MonsterDropItem(10002, 0.3f, 2, 5));
            monster.monsterDropItems.Add(new MonsterDropItem(10003, 0.1f, 1, 1));
            monster.dropItemProbAccum = new float[] { 0.25f, 0.55f, 0.65f, 1.0f };

            adpt = new MonsterAdapter(monster);
            dropItem = new DropItem(adpt);
        }

        [Test]
        public void test_calcDiffWithItemProb()
        {
            float randomFloatValue = 0.3f;

            float[] diffWithItemProb = dropItem.calcDiffWithItemProb(monster, randomFloatValue);

            for (int i = 0; i < diffWithItemProb.Length; i++)
            {
                Debug.Log("diffWithItemProb " + i + "th value: " + diffWithItemProb[i]);
            }

            // 0.3 - 0.25 = 0.05이고, 0.3 - 0.55 = -0.25, 오차 허용범위는 0.0001f이므로, 값은 아래 범위여야 함
            Assert.That(diffWithItemProb[0] > 0.0499 && diffWithItemProb[0] < 0.0501);

            // 0.3 - 0.55 = 0.25이고, 0.3 - 0.65 = -0.35, 오차 허용범위는 0.0001f, 값은 아래 범위여야 함
            Assert.That(diffWithItemProb[1] > 0.2499 && diffWithItemProb[1] < 0.2501);

            // 0.3 - 0.65 = -0.35이고, 0.3 - 1.0 = -0.7, 오차 허용범위는 0.0001f, 값은 아래 범위여야 함
            Assert.That(diffWithItemProb[2] > 0.3499 && diffWithItemProb[2] < 0.3501);
        }

        [Test]
        public void test_isNotSpawn()
        {
            // 아이템 Spawn
            Assert.That(dropItem.isNotSpawn(0.65001f, 3) == true);
            // 아이템 Not Spawn
            Assert.That(dropItem.isNotSpawn(0.64999f, 3) == false);
        }

        [Test]
        public void test_isSingleItem()
        {
            Assert.That(dropItem.isSingleItem(0) == false);
            Assert.That(dropItem.isSingleItem(1) == false);
            Assert.That(dropItem.isSingleItem(2) == true);
        }

        [Test]
        public void test_returnMinIntevalIndex()
        {
            Assert.That(dropItem.returnMinIntevalIndex(new float[] { 0.05f, 0.25f, 0.35f }) == 0);
        }


        [Test]
        public void test_decideRandomItemValues()
        {
            for (int i = 0; i < 100000; i++)
            {
                Assert.That(dropItem.decideRandomItemValues(0) >= 1 && dropItem.decideRandomItemValues(0) <= 3);
                Assert.That(dropItem.decideRandomItemValues(1) >= 2 && dropItem.decideRandomItemValues(1) <= 5);
                Assert.That(dropItem.decideRandomItemValues(2) == 1);
            }
        }

        [Test]
        public void test_decideRandomSpawnItem()
        {
            for (int i = 0; i < 100000; i++)
            {
                Assert.That(dropItem.decideRandomSpawnItem() >= 0f && dropItem.decideRandomSpawnItem() <= 1f);
            }
        }

    }
}