
package com.example.chatbot;

import com.google.firebase.database.IgnoreExtraProperties;


    @IgnoreExtraProperties
    public class Artist {
        private String artistId;
        private String artistName;
        private String artistAge;
        private String artistdob;
        private String artistincome;

        public Artist(){
            //this constructor is required
        }

        public Artist(String artistId, String artistName,String artistAge,String artistdob,String artistincome ) {
            this.artistId = artistId;
            this.artistName = artistName;
            this.artistAge = artistAge;
            this.artistdob = artistdob;
            this.artistincome = artistincome;
        }

        public String getArtistId() {
            return artistId;
        }

        public String getArtistAge() {
            return artistAge;
        }

        public String getArtistIncome() {
            return artistincome;
        }
        public String getArtistName() {
            return artistName;
        }

        public String getArtistdob() {
            return artistdob;
        }
    }

