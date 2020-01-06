package cn.dujc.core.initializer.refresh;

public interface IRefreshSetup {

    public IRefresh create();

    public IRefresh createList();

    public static class Impl implements IRefreshSetup {
        @Override
        public IRefresh create() {
            return new IRefresh.Impl();
        }

        @Override
        public IRefresh createList() {
            return new IRefresh.ListImpl();
        }
    }
}
