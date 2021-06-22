package com.resolver.anotation;

import java.lang.annotation.*;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface Pagination {

    /**
     * @return index of page
     */
    int page() default 0;

    /**
     * @return number of object in a page
     */
    int perPage() default 10;

    /**
     *
     * @return sort by which field of object
     */

    String sortBy() default "";

    /**
     * @return descending or ascending
     */
    String sortType() default "asc";
}
