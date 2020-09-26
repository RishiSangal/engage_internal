package com.example.sew.common;

public class Enums {
    public enum BOTTOM_TYPE {HOME_1, HOME_2, HOME_3, HOME_4}

    public enum LANGUAGE {
        ENGLISH, HINDI, URDU
    }
    public enum SORT_CONTENT{
        POPULARITY("0"),ALPHABETIC("1"),RADEEF("2");
        String key;
        SORT_CONTENT(String key){
            this.key= key;
        }
        public static SORT_CONTENT getSortContent(String key){
            if(key.contentEquals(POPULARITY.key))
                return POPULARITY;
            else if(key.contentEquals(ALPHABETIC.key))
                return ALPHABETIC;
            else if(key.contentEquals(RADEEF.key))
                return RADEEF;
            return POPULARITY;
        }
        public String getKey(){
            return key;
        }
    }
    public  enum FORUM_SORT_FIELDS{
        COMMENT_BY_REKHTA("0"),POPULARITY ("1"),RECENT_COMMENT("2");
        String key;

        FORUM_SORT_FIELDS(String key) {
            this.key = key;
        }
        public static FORUM_SORT_FIELDS getForumSortFieldsFromKey(String key) {
            if (key.contentEquals(COMMENT_BY_REKHTA.key))
                return COMMENT_BY_REKHTA;
            else if (key.contentEquals(POPULARITY.key))
                return POPULARITY;
            else if (key.contentEquals(RECENT_COMMENT.key))
                return RECENT_COMMENT;

            return COMMENT_BY_REKHTA;
        }
        public String getKey() {
            return key;
        }
    }
    public  enum COMMENT_SORT_LIST{
       DESCENDING ("0"),ASCENDING("1");
        String key;

        COMMENT_SORT_LIST(String key) {
            this.key = key;
        }
        public static COMMENT_SORT_LIST getCommentSortListFromKey(String key) {
            if (key.contentEquals(DESCENDING.key))
                return DESCENDING;
            else if (key.contentEquals(ASCENDING.key))
                return ASCENDING;

            return ASCENDING;
        }
        public String getKey() {
            return key;
        }
    }
    public enum MARK_STATUS_LIKE_DISLIKE {
        DISLIKE("0"), LIKE("1"),REMOVE_STATUS("2");
        String key;
        MARK_STATUS_LIKE_DISLIKE(String key) {
            this.key = key;
        }
        public static MARK_STATUS_LIKE_DISLIKE getMarkStatusFromKey(String key) {
            if (key.contentEquals(DISLIKE.key))
                return DISLIKE;
            else if (key.contentEquals(LIKE.key))
                return LIKE;
            else if (key.contentEquals(REMOVE_STATUS.key))
                return REMOVE_STATUS;

            return LIKE;
        }
        public String getKey() {
            return key;
        }
    }

    public enum COLLECTION_TYPE {PROSE, SHAYARI}

    public enum CONTENT_TYPE {GHAZAL, NAZM, SHER,POET}

    public enum LIST_RENDERING_FORMAT {GHAZAL, NAZM, SHER, AUDIO, VIDEO, PROFILE,IMAGE_SHAYRI}

    public enum TEXT_ALIGNMENT {LEFT, CENTER}

    public enum CRITIQUE_TYPE {FEEDBACK, CRITIQUE}

    public enum PLACEHOLDER_TYPE {PROFILE, PROFILE_LARGE, SHAYARI_IMAGE, SHAYARI_COLLECTION, PROMOTIONAL_BANNER}

    public enum SHER_COLLECTION_TYPE {TOP_20, OCCASIONS, TAG, OTHER}

    public enum PROSE_SHAYARI_CATEGORY {POET, COLLECTION}

    public enum DID_YOU_KNOW_TYPE_NAME {
        ENTITY("Entity"), EBOOK("Ebook"), GENERAL("General");
        String key;

        DID_YOU_KNOW_TYPE_NAME(String key) {
            this.key = key;
        }

        public static DID_YOU_KNOW_TYPE_NAME getTypeName(String key) {
            if (key.contentEquals(ENTITY.key))
                return ENTITY;
            else if (key.contentEquals(EBOOK.key))
                return EBOOK;
            else if (key.contentEquals(GENERAL.key))
                return GENERAL;
            return GENERAL;
        }
    }
    public enum  DARK_MODE{
        NIGHT_YES(0), NIGHT_NO(1), NIGHT_AUTO(2);
        int key;
        DARK_MODE(int key){
            this.key= key;
        }
        public static DARK_MODE getDarkModeKey(int key){
            if(key==NIGHT_YES.key)
                return NIGHT_YES;
            if(key==NIGHT_NO.key)
                return NIGHT_NO;
            if(key==NIGHT_AUTO.key)
                return NIGHT_YES;
            return NIGHT_NO;
        }
    }

    public enum FAV_TYPES {
        CONTENT("0"), IMAGE_SHAYRI("3"), WORD("4"), T20("5"), OCCASION("6"), SHAYARI_COLLECTION("7"), PROSE_COLLECTION("9"), ENTITY("15");
        String key;

        FAV_TYPES(String key) {
            this.key = key;
        }

        public static FAV_TYPES getFavTypeFromKey(String key) {
            if (key.contentEquals(CONTENT.key))
                return CONTENT;
            else if (key.contentEquals(IMAGE_SHAYRI.key))
                return IMAGE_SHAYRI;
            else if (key.contentEquals(WORD.key))
                return WORD;
            else if (key.contentEquals(T20.key))
                return T20;
            else if (key.contentEquals(OCCASION.key))
                return OCCASION;
            else if (key.contentEquals(SHAYARI_COLLECTION.key))
                return SHAYARI_COLLECTION;
            else if (key.contentEquals(PROSE_COLLECTION.key))
                return PROSE_COLLECTION;
            else if(key.equalsIgnoreCase(ENTITY.key))
                return ENTITY;
            return CONTENT;
        }

        public String getKey() {
            return key;
        }
    }

//    Content = 0, ImageShayari = 3, Word = 4, T20 = 5,
//    Occasion = 6, ShayariCollection = 7, ProseCollection = 9
}
