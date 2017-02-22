package com.sleepeasysoftware.platetoccd;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

/**
 * Created by Daniel Kaplan on behalf of Sleep Easy Software.
 */
public class DataWriterTest {

    private DataWriter subject;

    @Before
    public void setUp() throws Exception {
        subject = new DataWriter();

    }

    @Test
    public void getInputRowIndex() throws Exception {
        assertThat(subject.getInputRowIndex(0, 0), equalTo(0));
        assertThat(subject.getInputRowIndex(0, 3), equalTo(3));
        assertThat(subject.getInputRowIndex(1, 3), equalTo(4));
        assertThat(subject.getInputRowIndex(16, 0), equalTo(0));
        assertThat(subject.getInputRowIndex(16, 3), equalTo(3));
        assertThat(subject.getInputRowIndex(17, 0), equalTo(1));
        assertThat(subject.getInputRowIndex(383, 3), equalTo(18));
        assertThat(subject.getInputRowIndex(384, 3), equalTo(22));
    }

    @Test
    public void getInputColumnIndex() throws Exception {
        assertThat(subject.getInputColumnIndex(0), equalTo(0));
        assertThat(subject.getInputColumnIndex(1), equalTo(0));
        assertThat(subject.getInputColumnIndex(16), equalTo(1));
        assertThat(subject.getInputColumnIndex(17), equalTo(1));
        assertThat(subject.getInputColumnIndex(383), equalTo(23));
        assertThat(subject.getInputColumnIndex(384), equalTo(0));
    }
}