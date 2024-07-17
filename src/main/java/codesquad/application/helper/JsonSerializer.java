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
        if (postResponse.nickname() != null) {
            json.append("\"").append(postResponse.nickname()).append("\"");
        } else {
            json.append("null");
        }

        json.append(",\"content\":");
        if (postResponse.content() != null) {
            json.append("\"").append(postResponse.content()).append("\"");
        } else {
            json.append("null");
        }

        json.append(",\"imageName\":");
        if (postResponse.imageName() != null) {
            json.append("\"").append(postResponse.imageName()).append("\"");
        } else {
            json.append("null");
        }

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
        if (commentResponse.nickname() != null) {
            json.append("\"").append(commentResponse.nickname()).append("\"");
        } else {
            json.append("null");
        }

        json.append(",\"content\":");
        if (commentResponse.content() != null) {
            json.append("\"").append(commentResponse.content()).append("\"");
        } else {
            json.append("null");
        }

        json.append(",\"createdAt\":\"");
        json.append(commentResponse.createdAt().format(formatter));
        json.append("\"");

        json.append("}");
        return json.toString();
    }
}
