package org.osehra.integration.jms.service;

import org.osehra.integration.util.NullChecker;

import java.util.Map;

import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueReceiver;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Utility to send/receive from a queue.
 * 
 * @author Julian Jewel
 */
public class JmsUtility {

	/**
	 * Logger.
	 */
	private static final Log LOG = LogFactory.getLog(JmsUtility.class);

	/**
	 * Receive message from a queue.
	 * 
	 * @param ctx
	 *            the context
	 * @param queueConnectionFactory
	 *            the queue connection factory
	 * @param jndiName
	 *            the jndi name of the queue
	 * @param timeOut
	 *            the timeout
	 * @param selector
	 *            the selector
	 * @return the JMS message
	 */
	public static Message receive(final Context ctx,
			final String queueConnectionFactory, final String jndiName,
			final long timeOut, final String selector) {
		if (NullChecker.isEmpty(jndiName)) {
			throw new RuntimeException("JNDI name cannot be null");
		}

		if (NullChecker.isEmpty(queueConnectionFactory)) {
			throw new RuntimeException(
					"Queue Connection Factory cannot be null!");
		}

		QueueConnectionFactory qcf = null;
		QueueConnection connection = null;
		QueueReceiver receiver = null;
		QueueSession session = null;
		boolean contextCreated = false;
		Context newCtx = ctx;

		try {
			if (NullChecker.isEmpty(newCtx)) {
				newCtx = new InitialContext();
				contextCreated = true;
			}
			final Object boundQueue = newCtx.lookup(jndiName);
			final Object boundQcf = newCtx.lookup(queueConnectionFactory);

			if (!QueueConnectionFactory.class.isInstance(boundQcf)) {
				throw new RuntimeException(
						"Only javax.jms.QueueConnectionFactory is supported!");
			}

			if (!Queue.class.isInstance(boundQueue)) {
				throw new RuntimeException("Only javax.jms.Queue is supported!");
			}

			final Queue queue = (Queue) boundQueue;
			qcf = (QueueConnectionFactory) boundQcf;
			connection = (QueueConnection) qcf.createConnection();
			session = connection.createQueueSession(false, -1);

			if (NullChecker.isNotEmpty(selector)) {
				receiver = session.createReceiver(queue, selector);
			} else {
				receiver = session.createReceiver(queue);
			}

			connection.start();
			final Message message = receiver.receive(timeOut);
			connection.stop();
			return message;
		} catch (final NamingException ex) {
			throw new RuntimeException(ex);
		} catch (final JMSException ex) {
			throw new RuntimeException(ex);
		} finally {
			try {
				if (NullChecker.isNotEmpty(receiver)) {
					receiver.close();
				}
				if (NullChecker.isNotEmpty(session)) {
					session.close();
				}
				if (NullChecker.isNotEmpty(connection)) {
					connection.close();
				}
				if (contextCreated && NullChecker.isNotEmpty(newCtx)) {
					newCtx.close();
				}

			} catch (final NamingException ex) {
				if (JmsUtility.LOG.isErrorEnabled()) {
					JmsUtility.LOG.error(
							"Naming Exception when looking up the context", ex);
				}
			} catch (final JMSException ex) {
				if (JmsUtility.LOG.isErrorEnabled()) {
					JmsUtility.LOG
							.error("JMS Exception when trying to send the message",
									ex);
				}
			}
		}

	}

	/**
	 * Send bytes message.
	 * 
	 * @param bytes
	 *            the bytes
	 * @param queueConnectionFactory
	 *            the connection factory
	 * @param jndiName
	 *            the jndi name
	 * @param properties
	 *            the properties
	 */
	public static void sendBytesMessage(final byte[] bytes,
			final String queueConnectionFactory, final String jndiName,
			final Map<String, Object> properties) {

		JmsUtility.sendMessage(null, bytes, queueConnectionFactory, jndiName,
				properties, MessageType.BYTES);
	}

	/**
	 * Send bytes message.
	 * 
	 * @param ctx
	 *            the context
	 * @param bytes
	 *            the bytes
	 * @param queueConnectionFactory
	 *            the connection factory
	 * @param jndiName
	 *            the jndi name
	 * @param properties
	 *            the properties
	 */
	public static void sendBytesMessage(final Context ctx, final byte[] bytes,
			final String queueConnectionFactory, final String jndiName,
			final Map<String, Object> properties) {

		JmsUtility.sendMessage(ctx, bytes, queueConnectionFactory, jndiName,
				properties, MessageType.BYTES);
	}

	/**
	 * Send a message to a queue.
	 * 
	 * @param newCtx
	 *            the context
	 * @param bytes
	 *            the bytes message
	 * @param queueConnectionFactory
	 *            the connection factory
	 * @param jndiName
	 *            the jndi name
	 * @param properties
	 *            the properties
	 * @param messageType
	 *            the message type
	 */
	private static void sendMessage(final Context newCtx, final byte[] bytes,
			final String queueConnectionFactory, final String jndiName,
			final Map<String, Object> properties, final MessageType messageType) {

		if (NullChecker.isEmpty(jndiName)) {
			throw new RuntimeException("JNDI name cannot be null");
		}

		if (NullChecker.isEmpty(queueConnectionFactory)) {
			throw new RuntimeException(
					"Queue Connection Factory cannot be null!");
		}

		QueueConnectionFactory qcf = null;
		QueueConnection connection = null;
		QueueSender sender = null;
		QueueSession session = null;
		boolean contextCreated = false;
		Context ctx = newCtx;
		try {
			if (NullChecker.isEmpty(ctx)) {
				ctx = new InitialContext();
				contextCreated = true;
			}
			final Object boundQueue = ctx.lookup(jndiName);
			final Object boundQcf = ctx.lookup(queueConnectionFactory);

			if (!QueueConnectionFactory.class.isInstance(boundQcf)) {
				throw new RuntimeException(
						"Only javax.jms.QueueConnectionFactory is supported!");
			}

			if (!Queue.class.isInstance(boundQueue)) {
				throw new RuntimeException("Only javax.jms.Queue is supported!");
			}

			final Queue queue = (Queue) boundQueue;
			qcf = (QueueConnectionFactory) boundQcf;
			connection = (QueueConnection) qcf.createConnection();
			session = connection.createQueueSession(false, -1);
			Message message = null;
			if (MessageType.BYTES.equals(messageType)) {
				message = session.createBytesMessage();
				((BytesMessage) message).writeBytes(bytes);
			} else if (MessageType.TEXT.equals(messageType)) {
				message = session.createTextMessage(new String(bytes));
				// } else if (MessageType.XML.equals(messageType)) {
				// message = ((WLQueueSession) session)
				// .createXMLMessage(new String(bytes));
			} else {
				throw new RuntimeException("Unsupported message type!");
			}

			// Copy properties
			if (NullChecker.isNotEmpty(properties)) {
				for (final Map.Entry<String, Object> entry : properties
						.entrySet()) {
					final String key = entry.getKey();
					final Object value = entry.getValue();
					if (!NullChecker.isEmpty(value)) {
						message.setObjectProperty(key, value);
					}
				}
			}
			sender = session.createSender(queue);
			sender.send(message);
		} catch (final NamingException ex) {
			throw new RuntimeException(ex);
		} catch (final JMSException ex) {
			throw new RuntimeException(ex);
		} finally {
			try {
				if (!NullChecker.isEmpty(sender)) {
					sender.close();
				}
				if (!NullChecker.isEmpty(session)) {
					session.close();
				}
				if (!NullChecker.isEmpty(connection)) {
					connection.close();
				}
				if (contextCreated && !NullChecker.isEmpty(ctx)) {
					ctx.close();
				}

			} catch (final NamingException ex) {
				if (JmsUtility.LOG.isErrorEnabled()) {
					JmsUtility.LOG.error(
							"Naming Exception when looking up the context", ex);
				}
			} catch (final JMSException ex) {
				if (JmsUtility.LOG.isErrorEnabled()) {
					JmsUtility.LOG
							.error("JMS Exception when trying to send the message",
									ex);
				}
			}
		}
	}

	/**
	 * Send a message to the queue.
	 * 
	 * @param newCtx
	 *            the new context
	 * @param message
	 *            the message
	 * @param queueConnectionFactory
	 *            the queue connection factory
	 * @param jndiName
	 *            the jndi name
	 * @param properties
	 *            the properties
	 */
	public static void sendMessage(final Context newCtx, final Message message,
			final String queueConnectionFactory, final String jndiName,
			final Map<String, Object> properties) {

		if (NullChecker.isEmpty(jndiName)) {
			throw new RuntimeException("JNDI name cannot be null");
		}

		if (NullChecker.isEmpty(queueConnectionFactory)) {
			throw new RuntimeException(
					"Queue Connection Factory cannot be null!");
		}

		if (NullChecker.isEmpty(message)) {
			throw new RuntimeException("Message cannot be null!");
		}

		QueueConnectionFactory qcf = null;
		QueueConnection connection = null;
		QueueSender sender = null;
		QueueSession session = null;
		boolean contextCreated = false;
		Context ctx = newCtx;
		try {
			if (NullChecker.isEmpty(ctx)) {
				ctx = new InitialContext();
				contextCreated = true;
			}
			final Object boundQueue = ctx.lookup(jndiName);
			final Object boundQcf = ctx.lookup(queueConnectionFactory);

			if (!QueueConnectionFactory.class.isInstance(boundQcf)) {
				throw new RuntimeException(
						"Only javax.jms.QueueConnectionFactory is supported!");
			}

			if (!Queue.class.isInstance(boundQueue)) {
				throw new RuntimeException("Only javax.jms.Queue is supported!");
			}

			final Queue queue = (Queue) boundQueue;
			qcf = (QueueConnectionFactory) boundQcf;
			connection = (QueueConnection) qcf.createConnection();
			session = connection.createQueueSession(false, -1);

			// Copy properties
			if (NullChecker.isNotEmpty(properties)) {
				for (final Map.Entry<String, Object> entry : properties
						.entrySet()) {
					final String key = entry.getKey();
					final Object value = entry.getValue();
					if (!NullChecker.isEmpty(value)) {
						message.setObjectProperty(key, value);
					}
				}
			}
			sender = session.createSender(queue);
			sender.send(message);
		} catch (final NamingException ex) {
			throw new RuntimeException(ex);
		} catch (final JMSException ex) {
			throw new RuntimeException(ex);
		} finally {
			try {
				if (!NullChecker.isEmpty(sender)) {
					sender.close();
				}
				if (!NullChecker.isEmpty(session)) {
					session.close();
				}
				if (!NullChecker.isEmpty(connection)) {
					connection.close();
				}
				if (contextCreated && !NullChecker.isEmpty(ctx)) {
					ctx.close();
				}

			} catch (final NamingException ex) {
				if (JmsUtility.LOG.isErrorEnabled()) {
					JmsUtility.LOG.error(
							"Naming Exception when looking up the context", ex);
				}
			} catch (final JMSException ex) {
				if (JmsUtility.LOG.isErrorEnabled()) {
					JmsUtility.LOG
							.error("JMS Exception when trying to send the message",
									ex);
				}
			}
		}
	}

	/**
	 * Send text message.
	 * 
	 * @param ctx
	 *            the context
	 * @param textContent
	 *            the text content
	 * @param queueConnectionFactory
	 *            the connection factory
	 * @param jndiName
	 *            the jndi name
	 * @param properties
	 *            the properties
	 */
	public static void sendTextMessage(final Context ctx,
			final String textContent, final String queueConnectionFactory,
			final String jndiName, final Map<String, Object> properties) {

		JmsUtility.sendMessage(ctx, textContent.getBytes(),
				queueConnectionFactory, jndiName, properties, MessageType.TEXT);
	}

	/**
	 * Send text message.
	 * 
	 * @param textContent
	 *            the text content
	 * @param queueConnectionFactory
	 *            the connection factory
	 * @param jndiName
	 *            the jndi name
	 * @param properties
	 *            the properties
	 */
	public static void sendTextMessage(final String textContent,
			final String queueConnectionFactory, final String jndiName,
			final Map<String, Object> properties) {

		JmsUtility.sendMessage(null, textContent.getBytes(),
				queueConnectionFactory, jndiName, properties, MessageType.TEXT);
	}

	/**
	 * Send bytes message.
	 * 
	 * @param bytes
	 *            the bytes content
	 * @param queueConnectionFactory
	 *            the connection factory
	 * @param jndiName
	 *            the jndi name
	 * @param properties
	 *            the properties
	 */
	public static void sendXmlMessage(final byte[] bytes,
			final String queueConnectionFactory, final String jndiName,
			final Map<String, Object> properties) {

		JmsUtility.sendMessage(null, bytes, queueConnectionFactory, jndiName,
				properties, MessageType.XML);
	}

	/**
	 * Send bytes message.
	 * 
	 * @param ctx
	 *            the context
	 * @param bytes
	 *            the bytes content
	 * @param queueConnectionFactory
	 *            the connection factory
	 * @param jndiName
	 *            the jndi name
	 * @param properties
	 *            the properties
	 */
	public static void sendXmlMessage(final Context ctx, final byte[] bytes,
			final String queueConnectionFactory, final String jndiName,
			final Map<String, Object> properties) {

		JmsUtility.sendMessage(ctx, bytes, queueConnectionFactory, jndiName,
				properties, MessageType.XML);
	}

	/**
	 * Default protected constructor.
	 */
	protected JmsUtility() {
	}

}
