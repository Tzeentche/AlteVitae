package item;

import client.Client;

import java.util.Collections;
import java.util.Map;

@Service
public class Item extends Client {

    private static final String API_PREFIX = "/items";

    @Autowired
    public Item(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> createItem(long userId, ItemDto itemDto) {
        return post("", userId, itemDto);
    }

    public ResponseEntity<Object> getItem(long itemId, Long userId) {
        return get("/" + itemId, userId);
    }

    public ResponseEntity<Object> editItem(Long userId, long itemId, ItemDto itemDto) {
        return patch("/" + itemId, userId, itemDto);
    }

    public ResponseEntity<Object> getListOfItems(long userId, Long from, Long size) {
        if(from == null || size == null) {
            return get("", userId);
        }
        Map<String, Object> parameters = Map.of(
                "from", from,
                "size", size
        );
        return get("?from={from}&size={size}", userId, parameters);
    }

    public ResponseEntity<Object> getListOfItemsBySearch(String text, Long from, Long size) {
        if (text.isBlank()) {
            return new ResponseEntity<>(Collections.emptyList(), HttpStatus.OK);
        }

        if (from == null || size == null) {
            return get("/search?text=" + text);
        }

        Map<String, Object> parameters = Map.of(
                "text", text,
                "from", from,
                "size", size
        );
        return get("/search?text={text}&?from={from}&?size", null , parameters);
    }

    public ResponseEntity<Object> addCommentToItem(Long userId, Long itemId, CommentDto commentDto) {
        return post(String.format("/%s/comment", itemId), userId, commentDto);
    }
}
