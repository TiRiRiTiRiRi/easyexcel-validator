package com.personnel.common.base.vo;

import cn.hutool.core.convert.Convert;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.personnel.common.base.qo.PagingQo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 分页VO
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PagingVo<T> extends BaseVo {


    /**
     * 总数据量
     */
    private Long total;

    /**
     * 每页条数
     */
    private Long count;

    /**
     * 当前页码
     */
    private Long page;

    /**
     * 总页数
     */
    private Long totalPage;

    /**
     * 当前页数据
     */
    private List<T> items;

    private PagingVo() {
    }

    public PagingVo(IPage<T> page) {
        this.total = page.getTotal();
        this.count = page.getSize();
        this.page = page.getCurrent();
        this.totalPage = page.getPages();
        this.items = page.getRecords();
    }

    /**
     * 转换类型
     *
     * @param <R>
     * @param mapper
     * @return
     */
    public <R> PagingVo<R> convert(Function<? super T, ? extends R> mapper) {
        PagingVo<R> pagingVo = new PagingVo<>();
        pagingVo.setTotal(this.total);
        pagingVo.setCount(this.count);
        pagingVo.setPage(this.page);
        pagingVo.setTotalPage(this.totalPage);
        List<R> collect = this.items.stream().map(mapper).collect(Collectors.toList());
        pagingVo.setItems(collect);
        return pagingVo;
    }

    /**
     * 转换类型
     *
     * @param clazz<T> 目标类型
     * @return 返回转换后的目标类型对象
     */
    public <V> PagingVo<V> convert(Class<V> clazz) {
        PagingVo<V> pagingVo = new PagingVo<>();
        pagingVo.setTotal(this.total);
        pagingVo.setCount(this.count);
        pagingVo.setPage(this.page);
        pagingVo.setTotalPage(this.totalPage);
        pagingVo.setItems(this.items.stream().map(i -> Convert.convert(clazz, i)).toList());
        return pagingVo;
    }

    /**
     * 返回空分页
     *
     * @param <T>
     * @param qo
     * @return
     */
    public static <T> PagingVo<T> emptyResult(PagingQo qo) {
        PagingVo<T> pagingVo = new PagingVo<>();
        pagingVo.setTotal(0L);
        pagingVo.setTotalPage(0L);
        pagingVo.setCount((long) qo.getPageSize());
        pagingVo.setPage((long) qo.getPageNo());
        pagingVo.setItems(Collections.emptyList());
        return pagingVo;
    }

}
