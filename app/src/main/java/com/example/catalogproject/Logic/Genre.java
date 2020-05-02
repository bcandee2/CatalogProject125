package com.example.catalogproject.Logic;

import androidx.annotation.NonNull;

public enum Genre {
    BIOGRAPHY {
        /**
         * Gets Biography as string
         * @return "Biography
         */
        @NonNull
        @Override
        public String toString() {
            return "Biography";
        }
    }, FANTASY {
        /**
         * Gets Fantasy as string
         * @return "Fantasy
         */
        @NonNull
        @Override
        public String toString() {
            return "Fantasy";
        }
    }, HORROR {
        /**
         * Gets Horror as string
         * @return "Horror"
         */
        @NonNull
        @Override
        public String toString() {
            return "Horror";
        }
    }, MYSTERY {
        /**
         * Gets Mystery as string
         * @return "Mystery"
         */
        @NonNull
        @Override
        public String toString() {
            return "Mystery";
        }
    }, UNKNOWN{
        /**
         * Gets unknown genre as string
         * @return "???"
         */
        @NonNull
        @Override
        public String toString() {
            return "???";
        }
    };
}
