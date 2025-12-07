package image;

public class Divider {
    private final Image paddedImage;
    private final int resolution;

    public Divider(Image paddedImage, int blockSize) {
        this.paddedImage = paddedImage;
        this.resolution = blockSize;
    }

    public SubImage[][] divide() {
        int imgWidth = paddedImage.getWidth();
        int imgHeight = paddedImage.getHeight();

        int blockSize = imgWidth / resolution;
        int numBlockRows = imgHeight / blockSize;

        SubImage[][] subImages = new SubImage[numBlockRows][blockSize];

        for (int i = 0; i < numBlockRows; i++) {
            for (int j = 0; j < blockSize; j++) {
                int startX = j * resolution;
                int startY = i * resolution;

                int[][] blockPixels = new int[resolution][resolution];
                for (int y = 0; y < resolution; y++) {
                    for (int x = 0; x < resolution; x++) {
                        blockPixels[y][x] = paddedImage.getPixel(startX + x, startY + y);
                    }
                }

                subImages[i][j] = new SubImage(blockPixels);
            }
        }

        return subImages;
    }
}
