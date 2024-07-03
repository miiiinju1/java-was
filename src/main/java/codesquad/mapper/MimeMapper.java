package codesquad.mapper;

import codesquad.http.Mime;

import java.util.Arrays;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.stream.Collectors;

public class MimeMapper {

    public Mime getMimeFromAcceptHeader(String acceptHeader) {
        PriorityQueue<MimeWithQuality> mimeTypes = parseAcceptHeader(acceptHeader);

        if (mimeTypes.isEmpty()) {
            return Mime.TEXT_HTML;
        }

        MimeWithQuality mimeWithQuality = mimeTypes.poll();
        return mimeWithQuality.getMime();
    }

    private PriorityQueue<MimeWithQuality> parseAcceptHeader(String acceptHeader) {
        return Arrays.stream(acceptHeader.split(","))
                .map(part -> {
                    String[] subParts = part.split(";q=");
                    String mimeType = subParts[0].trim();
                    if(!Mime.isSupportType(mimeType)) {
                        return null;
                    }

                    Mime mime = Mime.ofType(mimeType);
                    double quality = subParts.length > 1 ? Double.parseDouble(subParts[1]) : 1.0;

                    return new MimeWithQuality(mime, quality);
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toCollection(() -> new PriorityQueue<>((o1, o2) -> Double.compare(o2.getQuality(), o1.getQuality()))));
    }


    private static class MimeWithQuality {
        private final Mime mime;
        private final double quality;

        public MimeWithQuality(Mime mime, double quality) {
            this.mime = mime;
            this.quality = quality;
        }

        public Mime getMime() {
            return mime;
        }

        public double getQuality() {
            return quality;
        }
    }
}
