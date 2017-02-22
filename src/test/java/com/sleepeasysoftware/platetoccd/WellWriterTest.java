package com.sleepeasysoftware.platetoccd;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

/**
 * Created by Daniel Kaplan on behalf of Sleep Easy Software.
 */
public class WellWriterTest {

    private WellWriter subject = new WellWriter();

    @Test
    public void alphabeticRollover() throws Exception {
        assertThat("first", subject.alphabeticRollover(0), equalTo('A'));
        assertThat(subject.alphabeticRollover(1), equalTo('B'));
        assertThat(subject.alphabeticRollover(15), equalTo('P'));
        assertThat("second", subject.alphabeticRollover(16), equalTo('A'));
    }

    @Test
    public void wellNumber() throws Exception {
        assertThat(subject.wellNumber(0), equalTo("01"));
        assertThat(subject.wellNumber(1), equalTo("01"));
        assertThat(subject.wellNumber(15), equalTo("01"));
        assertThat(subject.wellNumber(16), equalTo("02"));
        assertThat(subject.wellNumber(384), equalTo("01"));
        assertThat(subject.wellNumber(1151), equalTo("24"));
    }
}