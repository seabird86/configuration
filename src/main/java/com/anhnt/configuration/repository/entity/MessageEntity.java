package com.anhnt.configuration.repository.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;
import java.time.Instant;
import java.time.LocalDate;
import java.time.OffsetTime;

@Entity
@Table(name="message")
@Access(AccessType.FIELD)
@Getter
@Setter
@NoArgsConstructor
public class MessageEntity {
  @Id
  private String code;
  private String message;
  private String messageFr;
}
