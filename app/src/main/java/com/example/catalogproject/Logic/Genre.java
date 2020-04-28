package com.example.catalogproject.Logic;

import androidx.annotation.NonNull;

public enum Genre {
    BIOGRAPHY {
        @NonNull
        @Override
        public String toString() {
            return "Biography";
        }
    }, FANTASY {
        @NonNull
        @Override
        public String toString() {
            return "Fantasy";
        }
    }, HORROR {
        @NonNull
        @Override
        public String toString() {
            return "Horror";
        }
    }, MYSTERY {
        @NonNull
        @Override
        public String toString() {
            return "Mystery";
        }
    }, UNKNOWN{
        @NonNull
        @Override
        public String toString() {
            return "???";
        }
    };
}
