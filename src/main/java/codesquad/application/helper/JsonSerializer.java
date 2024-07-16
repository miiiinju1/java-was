package codesquad.application.helper;

import codesquad.application.domain.post.response.PostListResponse;
import codesquad.application.domain.post.response.PostResponse;

import java.util.List;
import java.util.stream.Collectors;


public class JsonSerializer {

    public static String toJson(PostListResponse postListResponse) {
        StringBuilder json = new StringBuilder();
        json.append("{");

        json.append("\"postResponses\":");
        json.append(toJson(postListResponse.postResponses()));

        json.append(",\"totalCount\":");
        json.append(postListResponse.totalCount());

        json.append("}");
        return json.toString();
    }

    private static String toJson(List<PostResponse> postResponses) {
        String jsonArray = postResponses.stream()
                .map(JsonSerializer::toJson)
                .collect(Collectors.joining(","));
        return "[" + jsonArray + "]";
    }

    private static String toJson(PostResponse postResponse) {
        StringBuilder json = new StringBuilder();
        json.append("{");

        json.append("\"postId\":");
        json.append(postResponse.postId());

        json.append(",\"nickname\":\"");
        json.append(postResponse.nickname());
        json.append("\"");

        json.append(",\"content\":\"");
        json.append(postResponse.content());
        json.append("\"");

        json.append(",\"imageName\":\"");
        json.append(postResponse.imageName());
        json.append("\"");

        json.append("}");
        return json.toString();
    }
}
