package com.kentisthebest.models;

import java.time.LocalDate;
import java.util.List;
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
public class MovieInfo {

  @Id
  private String id;
  private String name;
  private Integer year;
  private List<String> cast;
  private LocalDate releaseDate;

}
