package com.meelin.core.pagehelper;

import java.util.ArrayList;

/**
 * <p/>类文件: com.meelin.core.pagehelper.Page.java
 * <p/>类功能描述: Mybatis - 分页对象
 *
 * @作者: luj
 * @时间: 2016/12/20 15:03
 */
public class Page<E> extends ArrayList<E> {
    private static final long serialVersionUID = 1L;

    /**
     * 页码，从1开始
     */
    private int pageNum;
    /**
     * 页面大小
     */
//    private int pageSize;
    private int length;
    /**
     * 起始行
     */
//    private int startRow;
    private int start;
    /**
     * 末行
     */
    private int endRow;
    /**
     * 总数
     */
//    private int total;
    private int recordsTotal;
    /**
     * 总页数
     */
    private int pages;
    /**
     * 包含count查询
     */
    private boolean count;
    /**
     * count信号，3种情况，null的时候执行默认BoundSql,true的时候执行count，false执行分页
     */
    private Boolean countSignal;
    /**
     * 排序
     */
    private String orderBy;
    /**
     * 只增加排序
     */
    private boolean orderByOnly;
    /**
     * 分页合理化
     */
    private Boolean reasonable;
    /**
     * 当设置为true的时候，如果pagesize设置为0（或RowBounds的limit=0），就不执行分页，返回全部结果
     */
    private Boolean pageSizeZero;

    public Page() {
        super();
    }

    public Page(int pageNum, int pageSize) {
        this(pageNum, pageSize, true, null);
    }

    public Page(int pageNum, int pageSize, boolean count) {
        this(pageNum, pageSize, count, null);
    }

    private Page(int pageNum, int length, boolean count, Boolean reasonable) {
        super(0);
        if (pageNum == 1 && length == Integer.MAX_VALUE) {
            pageSizeZero = true;
            length = 0;
        }
        this.pageNum = pageNum;
        this.length = length;
        this.count = count;
        calculateStartAndEndRow();
        setReasonable(reasonable);
    }

    /**
     * int[] rowBounds
     * 0 : offset
     * 1 : limit
     */
    public Page(int[] rowBounds, boolean count) {
        super(0);
        if (rowBounds[0] == 0 && rowBounds[1] == Integer.MAX_VALUE) {
            pageSizeZero = true;
            this.length = 0;
        } else {
            this.length = rowBounds[1];
            this.pageNum = rowBounds[1] != 0 ? (int) (Math.ceil(((double) rowBounds[0] + rowBounds[1]) / rowBounds[1])) : 0;
        }
        this.start = rowBounds[0];
        this.count = count;
        this.endRow = this.start + rowBounds[1];
    }

    public int getPages() {
        return pages;
    }

    public Page<E> setPages(int pages) {
        this.pages = pages;
        return this;
    }

    public int getEndRow() {
        return endRow;
    }

    public Page<E> setEndRow(int endRow) {
        this.endRow = endRow;
        return this;
    }

    public int getPageNum() {
        return pageNum;
    }

    public Page<E> setPageNum(int pageNum) {
        //分页合理化，针对不合理的页码自动处理
        this.pageNum = ((reasonable != null && reasonable) && pageNum <= 0) ? 1 : pageNum;
        return this;
    }

    public int getLength() {
        return length;
    }

    public Page<E> setLength(int length) {
        this.length = length;
        return this;
    }

    public int getStart() {
        return start;
    }

    public Page<E> setStart(int start) {
        this.start = start;
        return this;
    }

    public int getRecordsTotal() {
        return recordsTotal;
    }

    public void setRecordsTotal(int recordsTotal) {
        this.recordsTotal = recordsTotal;
        if (recordsTotal == -1) {
            pages = 1;
            return;
        }
        if (length > 0) {
            pages = recordsTotal / length + ((recordsTotal % length == 0) ? 0 : 1);
        } else {
            pages = 0;
        }
        //分页合理化，针对不合理的页码自动处理
        if ((reasonable != null && reasonable) && pageNum > pages) {
            pageNum = pages;
            calculateStartAndEndRow();
        }
    }

    public Boolean getReasonable() {
        return reasonable;
    }

    public Page<E> setReasonable(Boolean reasonable) {
        if (reasonable == null) {
            return this;
        }
        this.reasonable = reasonable;
        //分页合理化，针对不合理的页码自动处理
        if (this.reasonable && this.pageNum <= 0) {
            this.pageNum = 1;
            calculateStartAndEndRow();
        }
        return this;
    }

    public Boolean getPageSizeZero() {
        return pageSizeZero;
    }

    public Page<E> setPageSizeZero(Boolean pageSizeZero) {
        if (pageSizeZero != null) {
            this.pageSizeZero = pageSizeZero;
        }
        return this;
    }

    /**
     * 计算起止行号
     */
    private void calculateStartAndEndRow() {
        this.start = this.pageNum > 0 ? (this.pageNum - 1) * this.length : 0;
        this.endRow = this.start + this.length * (this.pageNum > 0 ? 1 : 0);
    }

    public boolean isCount() {
        return this.count;
    }

    public Page<E> setCount(boolean count) {
        this.count = count;
        return this;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public <E> Page<E> setOrderBy(String orderBy) {
        this.orderBy = orderBy;
        return (Page<E>) this;
    }

    public boolean isOrderByOnly() {
        return orderByOnly;
    }

    public void setOrderByOnly(boolean orderByOnly) {
        this.orderByOnly = orderByOnly;
    }

    public Boolean getCountSignal() {
        return countSignal;
    }

    public void setCountSignal(Boolean countSignal) {
        this.countSignal = countSignal;
    }

    //增加链式调用方法

    /**
     * 设置页码
     *
     * @param pageNum
     * @return
     */
    public Page<E> pageNum(int pageNum) {
        //分页合理化，针对不合理的页码自动处理
        this.pageNum = ((reasonable != null && reasonable) && pageNum <= 0) ? 1 : pageNum;
        return this;
    }

    /**
     * 设置页面大小
     *
     * @param length
     * @return
     */
    public Page<E> pageSize(int length) {
        this.length = length;
        calculateStartAndEndRow();
        return this;
    }

    /**
     * 是否执行count查询
     *
     * @param count
     * @return
     */
    public Page<E> count(Boolean count) {
        this.count = count;
        return this;
    }

    /**
     * 设置合理化
     *
     * @param reasonable
     * @return
     */
    public Page<E> reasonable(Boolean reasonable) {
        setReasonable(reasonable);
        return this;
    }

    /**
     * 当设置为true的时候，如果pagesize设置为0（或RowBounds的limit=0），就不执行分页，返回全部结果
     *
     * @param pageSizeZero
     * @return
     */
    public Page<E> pageSizeZero(Boolean pageSizeZero) {
        setPageSizeZero(pageSizeZero);
        return this;
    }

    /**
     * 转换为PageInfo
     *
     * @return
     */
    public PageInfo<E> toPageInfo() {
        PageInfo<E> pageInfo = new PageInfo<E>(this);
        return pageInfo;
    }

    public <E> Page<E> doSelectPage(ISelect select) {
        select.doSelect();
        return (Page<E>) this;
    }

    public <E> PageInfo<E> doSelectPageInfo(ISelect select) {
        select.doSelect();
        return (PageInfo<E>) this.toPageInfo();
    }

    public long doCount(ISelect select) {
        this.pageSizeZero = true;
        this.length = 0;
        select.doSelect();
        return this.recordsTotal;
    }

    @Override
    public String toString() {
        return "Page{" +
                "count=" + count +
                ", pageNum=" + pageNum +
                ", length=" + length +
                ", start=" + start +
                ", endRow=" + endRow +
                ", recordsTotal=" + recordsTotal +
                ", pages=" + pages +
                ", countSignal=" + countSignal +
                ", orderBy='" + orderBy + '\'' +
                ", orderByOnly=" + orderByOnly +
                ", reasonable=" + reasonable +
                ", pageSizeZero=" + pageSizeZero +
                '}';
    }
}
