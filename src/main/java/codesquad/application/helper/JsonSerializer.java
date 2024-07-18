package codesquad.application.helper;

import codesquad.application.domain.comment.response.CommentListResponse;
import codesquad.application.domain.comment.response.CommentResponse;
import codesquad.application.domain.post.response.PostListResponse;
import codesquad.application.domain.post.response.PostResponse;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class JsonSerializer {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    public static String toJson(PostListResponse postListResponse) {
        StringBuilder json = new StringBuilder();
        json.append("{");

        json.append("\"postResponses\":");
        json.append(toJsonPostResponseList(postListResponse.postResponses()));

        json.append(",\"totalCount\":");
        json.append(postListResponse.totalCount());

        json.append("}");
        return json.toString();
    }

    private static String toJsonPostResponseList(List<PostResponse> postResponses) {
        String jsonArray = postResponses.stream()
                .map(JsonSerializer::toJsonPostResponse)
                .collect(Collectors.joining(","));
        return "[" + jsonArray + "]";
    }

    private static String toJsonPostResponse(PostResponse postResponse) {
        StringBuilder json = new StringBuilder();
        json.append("{");

        json.append("\"postId\":");
        json.append(postResponse.postId());

        json.append(",\"nickname\":");
        json.append(escapeJsonString(postResponse.nickname()));

        json.append(",\"content\":");
        json.append(escapeJsonString(postResponse.content()));

        json.append(",\"imageName\":");
        json.append(escapeJsonString(postResponse.imageName()));

        json.append(",\"commentList\":");
        json.append(toJsonCommentListResponse(postResponse.commentList()));

        json.append(",\"createdAt\":\"");
        json.append(postResponse.createdAt().format(formatter));
        json.append("\"");

        json.append("}");
        return json.toString();
    }

    private static String toJsonCommentListResponse(CommentListResponse commentListResponse) {
        StringBuilder json = new StringBuilder();
        json.append("{");

        json.append("\"postId\":");
        json.append(commentListResponse.postId());

        json.append(",\"commentList\":");
        json.append(toJsonCommentResponseList(commentListResponse.commentList()));

        json.append(",\"commentCount\":");
        json.append(commentListResponse.commentCount());

        json.append("}");
        return json.toString();
    }

    private static String toJsonCommentResponseList(List<CommentResponse> commentResponses) {
        String jsonArray = commentResponses.stream()
                .map(JsonSerializer::toJsonCommentResponse)
                .collect(Collectors.joining(","));
        return "[" + jsonArray + "]";
    }

    private static String toJsonCommentResponse(CommentResponse commentResponse) {
        StringBuilder json = new StringBuilder();
        json.append("{");

        json.append("\"commentId\":");
        json.append(commentResponse.commentId());

        json.append(",\"nickname\":");
        json.append(escapeJsonString(commentResponse.nickname()));

        json.append(",\"content\":");
        json.append(escapeJsonString(commentResponse.content()));

        json.append(",\"createdAt\":\"");
        json.append(commentResponse.createdAt().format(formatter));
        json.append("\"");

        json.append("}");
        return json.toString();
    }

    private static String escapeJsonString(String value) {
        if (value == null) {
            return "null";
        }
        return "\"" + value.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t") + "\"";
    }
}
