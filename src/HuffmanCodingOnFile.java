import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.BitSet;

public class HuffmanCodingOnFile extends HuffmanCode {
    //compress the file
    public void compressFile(String filePath) throws IOException {
        String input = Files.readString(Paths.get(filePath));
        String data = compress(input);
        String header = getLastOperationHeader();
        writeBinaryFile(filePath.substring(0, filePath.indexOf(".")).concat(".compressed"), header, data, true);
    }

    //decompress the target file
    public void decompressFile(String filePath) throws IOException {
        String[] headerData = readBinaryFile(filePath);
        String decompressed = decompress(headerData[0], headerData[1]);
        writeBinaryFile(filePath.substring(0, filePath.indexOf(".")).concat("_decompressed.txt"), decompressed, "",
                false);
    }

    //only rawstring written in binary
    public static void writeBinaryFile(String filePath, String data, String raw, boolean wrtieSize) {
        if (wrtieSize) {
            // adding the size of the raw input to the data to help in file reading
            data = raw.length() + SPILT_ELEMENT + data;
        }
        byte[] dataBytes = data.getBytes();
        byte[] rawBytes = stringToBitset(raw).toByteArray();
        try {
            Files.write(Paths.get(filePath), dataBytes);
            // added append option to prevent it from over write the previous data
            Files.write(Paths.get(filePath), rawBytes, StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //convert string to binary

    public static BitSet stringToBitset(String binary) {
        BitSet bitset = new BitSet(binary.length());
        int len = binary.length();
        for (int i = len - 1; i >= 0; i--) {
            if (binary.charAt(i) == '1') {
                bitset.set(len - i - 1);
            }
        }
        return bitset;
    }

    //convert binary to string data
    public static String bitsetToString(BitSet input, int size) {
        StringBuilder output = new StringBuilder();
        for (int i = size - 1; i >= 0; i--) {
            if (input.get(i)) {
                output.append("1");
            } else {
                output.append("0");
            }
        }
        return output.toString();
    }

    //read header and string data from target file
    public static String[] readBinaryFile(String filePath) throws IOException {
        String[] headerDataStrings = new String[2];
        // read the file into bytes array
        byte[] allBytes = Files.readAllBytes(Paths.get(filePath));
        // convert the bytes array to string to get the indices of start and end of
        // header string
        String allBytesString = new String(allBytes);
        // get the index of the size writen in the file
        int endSizeIndex = allBytesString.indexOf(SPILT_ELEMENT);
        // convert the size form string to integer
        int size = Integer.parseInt(allBytesString.substring(0, endSizeIndex));
        // get the start header index
        int startHeaderIndex = endSizeIndex + SPILT_ELEMENT.length();
        // get the end header index
        int endHeaderIndex = allBytesString.indexOf(TABLE_END_ELEMENT) + TABLE_END_ELEMENT.length();
        // get the header from the file
        headerDataStrings[0] = allBytesString.substring(startHeaderIndex, endHeaderIndex);
        // add the binary data to a separate bytes array for converting it to string
        byte[] dataBytes = new byte[allBytes.length - endHeaderIndex];
        System.arraycopy(allBytes, endHeaderIndex, dataBytes, 0, dataBytes.length);
        // converting the binary data to string
        headerDataStrings[1] = bitsetToString(BitSet.valueOf(dataBytes), size);
        // return the array of header and data strings
        return headerDataStrings;
    }
}
