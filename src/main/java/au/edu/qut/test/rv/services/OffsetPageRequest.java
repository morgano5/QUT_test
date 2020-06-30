package au.edu.qut.test.rv.services;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

class OffsetPageRequest implements Pageable {

  private final int offset;
  private final int limit;
  private final Sort sort;

  public OffsetPageRequest(int offset, int limit, Sort sort) {
    this.offset = offset;
    this.limit = limit;
    this.sort = sort;
  }

  @Override
  public int getPageNumber() {
    return offset / limit;
  }

  @Override
  public int getPageSize() {
    return limit;
  }

  @Override
  public long getOffset() {
    return offset;
  }

  @Override
  public Sort getSort() {
    return sort;
  }

  @Override
  public Pageable next() {
    return new OffsetPageRequest(offset + limit, limit, sort);
  }

  @Override
  public Pageable previousOrFirst() {
    int newOffset = offset - limit;
    return new OffsetPageRequest(Math.max(newOffset, 0), limit, sort);
  }

  @Override
  public Pageable first() {
    return null;
  }

  @Override
  public boolean hasPrevious() {
    return offset > limit;
  }
}
