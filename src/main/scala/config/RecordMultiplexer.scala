package config

import chisel.lib.dclib.{DCMirror, DCOutput}
import com.quarch.configgen.RecordDef
import chisel3._
import chisel3.util._
import chisel3.util.experimental.forceName

import scala.collection.immutable.{LinearSeq, ListMap}
import scala.collection.mutable

class UserRecord(r : RecordDef) extends Record {
  var portSeq = new mutable.ArraySeq[(String, Data)](0)

  for (rNum <- 0 until r.entryList.size()) {
    portSeq = portSeq :+ (r.entryList.get(rNum).Name, UInt(r.entryList.get(rNum).Width.intValue().W))
  }

  val elements = ListMap(portSeq: _*)

  def apply(elt: String): Data = elements(elt)
  override def cloneType: this.type = (new UserRecord(r)).asInstanceOf[this.type]
}

class RecordMultiplexer(outBuffer : Boolean, r : RecordDef) extends RawModule {
  val m = r.Outputs.intValue()
  val io = IO(new Bundle {
    val clk = Input(Clock())
    val reset = Input(Bool())
    val copyIn = Input(UInt(m.W))
    val recIn = Flipped(Decoupled(new UserRecord(r)))
    val recOut = Vec(m, Decoupled(new UserRecord(r)))
  })
  override def desiredName: String = r.Name.toString

  // Use forceName to eliminate the prefixes on signals
  forceName(io.clk,   "clk")
  forceName(io.reset, "reset")

  // Change the name of the incoming record elements to "In_"
  for (e <- io.recIn.elements) {
    forceName(io.recIn.elements(e._1).asUInt(), "In_"+e._1);
  }
  for (e <- io.recIn.bits.elements) {
    forceName(io.recIn.bits.elements(e._1).asUInt(), "In_"+e._1);
  }

  // Change the name of the outgoing elements to "Out1_" to "OutN".
  for (i <- 0 until m) {
    val prefix = "Out" + (i+1) + "_"
    for (e <- io.recOut(i).elements) {
      forceName(io.recOut(i).elements(e._1).asUInt(), prefix + e._1);
    }
    for (e <- io.recOut(i).bits.elements) {
      forceName(io.recOut(i).bits.elements(e._1).asUInt(), prefix + e._1);
    }
  }

  withClockAndReset(io.clk, io.reset) {
    val mirror = Module(new DCMirror(new UserRecord(r), m))

    mirror.io.c <> io.recIn
    mirror.io.dst := io.copyIn
    if (outBuffer) {
      for (i <- 0 until m) {
        io.recOut(i) <> DCOutput(mirror.io.p(i))
      }
    } else {
      io.recOut <> mirror.io.p
    }
  }
}
