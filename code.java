import org.json.JSONObject;
import org.json.JSONTokener;
import java.io.FileReader;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class ShamirSecretSharing {

    // Function to decode a value from a given base to decimal
    private static BigInteger decodeValue(String value, int base) {
        return new BigInteger(value, base);
    }

    // Function to perform Lagrange interpolation and find the constant term (c)
    private static BigInteger lagrangeInterpolation(List<BigInteger> xValues, List<BigInteger> yValues) {
        BigInteger c = BigInteger.ZERO;
        int k = xValues.size();

        for (int i = 0; i < k; i++) {
            BigInteger numerator = yValues.get(i);
            BigInteger denominator = BigInteger.ONE;

            for (int j = 0; j < k; j++) {
                if (i != j) {
                    numerator = numerator.multiply(xValues.get(j).negate());
                    denominator = denominator.multiply(xValues.get(i).subtract(xValues.get(j)));
                }
            }
            c = c.add(numerator.divide(denominator));
        }
        return c;
    }

    public static void main(String[] args) {
        try {
            // Read the JSON file
            FileReader reader = new FileReader("input.json");
            JSONObject json = new JSONObject(new JSONTokener(reader));

            // Extract n and k
            int n = json.getJSONObject("keys").getInt("n");
            int k = json.getJSONObject("keys").getInt("k");

            // Lists to store x and y values
            List<BigInteger> xValues = new ArrayList<>();
            List<BigInteger> yValues = new ArrayList<>();

            // Decode and store the roots
            for (int i = 1; i <= n; i++) {
                JSONObject root = json.getJSONObject(String.valueOf(i));
                int base = Integer.parseInt(root.getString("base"));
                String value = root.getString("value");

                BigInteger x = new BigInteger(String.valueOf(i));
                BigInteger y = decodeValue(value, base);

                xValues.add(x);
                yValues.add(y);
            }

            // Perform Lagrange interpolation to find the constant term (c)
            BigInteger secret = lagrangeInterpolation(xValues, yValues);
            System.out.println("The secret (c) is: " + secret);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
