package captcha;

import java.util.Random;

public final class Randoms
{
    private static final Random RANDOM = new Random();
    //定义验证码字符.去除了O和I等容易混淆的字母
    public static final char ALPHA[]={'A','B','C','D','E','F','G','H','G','K','M','N','P','Q','R','S','T','U','V','W','X','Y','Z'
            ,'a','b','c','d','e','f','g','h','i','j','k','m','n','p','q','r','s','t','u','v','w','x','y','z','2','3','4','5','6','7','8','9'};
 
    public static final char[] CONTENTS = new char[]{'中','国','人','民','解','放','军','抗','战','胜','利'};
    public static final String WORD = "么万三上下与习也之义久丸丈乞冰冲厌创刚刘刑兆亚匠防邪阳阴阵网劣企伞仰伐仿伏伙伤似伟伪伍休优协充亦访讽讲延芒芝巡州迈迁迅寺寻夺夹夸巩异庆庄帆师吃吓吉吗吐驰闭乡勺刃亏凡卫亿亡吴呀驳驴驱闷闲宏宋妨妙妥妖狂犹岔叉川寸弓巾士夕中书无不专为公六历切元五区队内办从今以化什计认反违迎远寿弃床库序希帐吧吵呈吹呆太天引开少比长车斗方风火见片气日手水王文支分丰乏乌丹予丑勾勿匀厅允互井云匹凤冈劝凶仓介仇仅仆仁仍升午订双友艺屯夫巨币尺扎巴忆幻尤孔贝父户斤木牛丙丛丝匆占厉刊兄兰印功击令付仙仪仔仗让讨讯训辽失央巧左归帅叨叼叮句古另叶司台叹右召闪宁奴犯尼扔汉汇汁纠圣幼冬孕轧灭斥末未旦旧礼永甘瓜禾矛鸟皮甲申田甩玉共决压争划列则光先阶那关再动军农会众传价件任全华产交论设许达过导并年当合各后名同向问安好如她江红级约场地在回团因多式存成观老机权收次有此百而米色西行至自乓乒乔丢买兴闯守宇宅妇妈妄岂岁屿尽壮扛扣扩扫托扬执池汗汤污纪纤圾尘尖忙孙字负贞毕轨危爷戏灯灰考朵朴杀朽杂朱欢旬早旨曲肌臣虫耳于个千及大飞干工广己已口马门山才土小子齐肉舌羽舟竹页衣血羊份两严况别利际即却劳但低何你体位住作克县识证花还进近连运这张应听员间完形层局声把报技没快我极来条改状时社求志更步每究系角里身走串丽乱兵冻冷冶初免龟判删医阿陈附邻陆邮阻助劫劲励努余伯伴佛估伶伸佣亩词评诉译诊苍芳芦芹苏芽彻役迟返吨否告含吼君启吞呜岛岗尾饭饮壳扮抄扯抖扶抚护拒抗扭抛批抢扰折投找抓沉泛沟汽沙沈汪沃纯纺纲纳纽纱纹纸纵坝坊坏坚均坑块坛址欠犬氏瓦牙止爪业东且世主包北加务写出代们他半去记议发节边对头平布市号叫可史只它打四外处本术民必正白立龙目生石示电由用卡册乎乐丘坐困围园怀忧孝财贡歼戒灿灵灾灶材村杜杆杠李束杏杨牢攻旱旷忌忍忘皂私秃秀钉针盯疗穷补良辰赤豆谷麦辛言足吩坟纷芬";
    /**
     * 产生两个数之间的随机数
     * @param min 小数
     * @param max 比min大的数
     * @return int 随机数字
     */
    public static int num(int min, int max)
    {
        return min + RANDOM.nextInt(max - min);
    }
 
    /**
     * 产生0--num的随机数,不包括num
     * @param num 数字
     * @return int 随机数字
     */
    public static int num(int num)
    {
        return RANDOM.nextInt(num);
    }
 
    public static char alpha()
    {
        return WORD.charAt(num(0, CONTENTS.length));
//        return CONTENTS[num(0, CONTENTS.length)];
    }
}