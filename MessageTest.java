package ClientStuff;

import org.junit.Test;

public class MessageTest {

    @Test
    public void timestamps() throws InterruptedException {
        int Alice_id = 1;
        int Bob_id = 2;

        Message aliceToBob = new Message(Alice_id, "Hi there");
        Thread.sleep(3000);
        Message bobToAlice = new Message(Bob_id, "How u doin?");


        System.out.println(aliceToBob.getSentDate().getTime());
        System.out.println(bobToAlice.getSentDate().getTime());

        long diff_milliseconds = bobToAlice.getSentDate().getTime() - aliceToBob.getSentDate().getTime();// aliceToBob.getSentDate();
        assert (diff_milliseconds >= 3000) && (diff_milliseconds <= 4000);
    }

    @Test
    public void correctness() {
        int user = 1;
        String text = "Hi there";
        Message message = new Message(user, text);
        assert (text.equals(message.getText()));
    }

    @Test
    public void reducement() {
        int user = 1;
        String text = "odfojofjvosdnkjnsdifnvijndfijvnisdjfnvijndfijnvisjdfnvijndsfijfvnisjdnfijvnsidjdfnijvnsdifjnvisdnfivjnsdfnvisnvivnennvienfivnidffnvijdiffnvidnfivjn";
        Message message = new Message(user, text);
        assert message.getSize() == 140;
    }

    @Test
    public void canModify() {
        int user = 1;
        String text = "before modification";
        Message msg = new Message(1, text);
        String after = "after modification";
        msg.modify(after);

        assert !msg.getText().equals(text);
        assert msg.getText().equals(after);
    }
}
