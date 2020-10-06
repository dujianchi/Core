package cn.dujc.core.easteregg;

/**
 * 彩蛋默认实现方案
 */
public class EasterEggImpl implements IEasterEgg {

    @Override
    public boolean canOpen() {
        return false;
    }

    @Override
    public void open() {

    }

    @Override
    public int[] trigger() {
        //点3下，滑动一下
        return new int[]{IEasterEgg.TAP, IEasterEgg.TAP, IEasterEgg.TAP, IEasterEgg.SLIDE};
    }
}
