package com.viroyal.doormagnet.common.mybatisplus.page;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;

/**
 * 分页 Page 对象接口
 *
 * @author hubin
 * @since 2018-06-09
 */
public interface IPage<T> extends Serializable {


    /**
     * 获取排序信息，排序的字段和正反序
     *
     * @return 排序信息
     */
    List<OrderItem> orders();

    /**
     * KEY/VALUE 条件
     *
     * @return ignore
     */
    default Map<Object, Object> condition() {
        return null;
    }

    /**
     * 自动优化 COUNT SQL�? 默认：true �?
     *
     * @return true �? / false �?
     */
    default boolean optimizeCountSql() {
        return true;
    }

    /**
     * 进行 count 查询 �? 默认: true �?
     *
     * @return true �? / false �?
     */
    default boolean isSearchCount() {
        return true;
    }

    /**
     * 计算当前分页偏移�?
     */
    default long offset() {
        return getCurrent() > 0 ? (getCurrent() - 1) * getSize() : 0;
    }

    /**
     * 当前分页总页�?
     */
    default long getPages() {
        if (getSize() == 0) {
            return 0L;
        }
        long pages = getTotal() / getSize();
        if (getTotal() % getSize() != 0) {
            pages++;
        }
        return pages;
    }

    /**
     * 内部�?么也不干
     * <p>只是为了 json 反序列化时不报错</p>
     */
    default IPage<T> setPages(long pages) {
        // to do nothing
        return this;
    }

    /**
     * 分页记录列表
     *
     * @return 分页对象记录列表
     */
    List<T> getRecords();

    /**
     * 设置分页记录列表
     */
    IPage<T> setRecords(List<T> records);

    /**
     * 当前满足条件总行�?
     *
     * @return 总条�?
     */
    long getTotal();

    /**
     * 设置当前满足条件总行�?
     */
    IPage<T> setTotal(long total);

    /**
     * 当前分页总页�?
     *
     * @return 总页�?
     */
    long getSize();

    /**
     * 设置当前分页总页�?
     */
    IPage<T> setSize(long size);

    /**
     * 当前页，默认 1
     *
     * @return 当前�?
     */
    long getCurrent();

    /**
     * 设置当前�?
     */
    IPage<T> setCurrent(long current);

    /**
     * IPage 的泛型转�?
     *
     * @param mapper 转换函数
     * @param <R>    转换后的泛型
     * @return 转换泛型后的 IPage
     */
    @SuppressWarnings("unchecked")
    default <R> IPage<R> convert(Function<? super T, ? extends R> mapper) {
        List<R> collect = this.getRecords().stream().map(mapper).collect(toList());
        return ((IPage<R>) this).setRecords(collect);
    }
}
