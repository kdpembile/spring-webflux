package com.kentisthebest.domains;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document
public class Review {

  @Id
  private String reviewId;

  private Long movieInfoId;
  private String comment;
  private Double rating;

}
