package ulb;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import ulb.models.Student;

public class TestStudent {

    @Test
    public void shouldBeAdult() {
        Student s = new Student(25);
        assertTrue(s.isAdult());
    }

    @Test
    public void shouldNotBeAdult() {
        Student s = new Student(15);
        assertFalse(s.isAdult());
    }
}
