package com.sourcefuse.jarc.services.notificationservice.providers.email.types;

import java.io.Serializable;

import com.sourcefuse.jarc.services.notificationservice.types.Message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public abstract class EmailMessage extends Message implements Serializable {

	private static final long serialVersionUID = -8676711101806618732L;

	private EmailReceiver receiver;
}
