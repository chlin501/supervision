package supervision

protected[supervision] sealed trait Message
protected[supervision] trait SupervisorMessage extends Message
protected[supervision] case object ServiceList extends SupervisorMessage
protected[supervision] case object RemovedService extends SupervisorMessage
protected[supervision] case object FailedService extends SupervisorMessage
protected[supervision] case object AddedService extends SupervisorMessage
protected[supervision] case object EndedService extends SupervisorMessage


