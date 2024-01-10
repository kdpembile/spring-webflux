package com.kentisthebest.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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

  @NotBlank(message = "movieInfo.name must be present")
  private String name;

  @NotNull
  @Positive(message = "movieInfo.year must be positive value")
  private Integer year;

  private List<@NotBlank(message = "movieInfo.cast must be present") String> cast;

  private LocalDate releaseDate;

}
