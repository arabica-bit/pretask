package moneyspray.Util;

import java.util.Random;

public class TokenNameUtil {

    public String getNewName() {
        StringBuffer temp = new StringBuffer();

        try{
            Random rnd = new Random();
            for (int i = 0; i < 3; i++) {
                int rIndex = rnd.nextInt(3);
                switch (rIndex) {
                    case 0:
                        // a-z
                        temp.append((char) ((int) (rnd.nextInt(26)) + 97));
                        break;
                    case 1:
                        // A-Z
                        temp.append((char) ((int) (rnd.nextInt(26)) + 65));
                        break;
                    case 2:
                        // 0-9
                        temp.append((rnd.nextInt(10)));
                        break;
                }
            }
        }catch (Exception e){
            return null;
        }


        return temp.toString();
    }
}
