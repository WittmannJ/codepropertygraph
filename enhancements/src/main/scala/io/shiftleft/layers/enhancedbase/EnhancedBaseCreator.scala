package io.shiftleft.layers.enhancedbase

import gremlin.scala.ScalaGraph
import io.shiftleft.passes.linking.callargumentlinker.CallArgumentLinker
import io.shiftleft.passes.linking.capturinglinker.CapturingLinker
import io.shiftleft.passes.linking.linker.Linker
import io.shiftleft.passes.linking.memberaccesslinker.MemberAccessLinker
import io.shiftleft.passes.methoddecorations.MethodDecoratorPass
import io.shiftleft.passes.namspacecreator.NamespaceCreator

class EnhancedBaseCreator(graph: ScalaGraph, language: String) {

  def create = {

    val methodDecorator = new MethodDecoratorPass(graph)
    methodDecorator.executeAndApply()

    linkingEnhancements(graph)

    // TODO: "contains"-edges are AST edges. Their existence has
    // historical reasons, and we should replace them by AST
    // edges in the future

    val namespaceCreator = new NamespaceCreator(graph)
    namespaceCreator.executeAndApply()

    val callArgumentLinker = new CallArgumentLinker(graph)
    callArgumentLinker.executeAndApply()
  }

  private def linkingEnhancements(graph: ScalaGraph): Unit = {
    val capturingLinker = new CapturingLinker(graph)
    capturingLinker.executeAndApply()

    val linker = new Linker(graph)
    linker.executeAndApply()

    val memberAccessLinker = new MemberAccessLinker(graph)
    memberAccessLinker.executeAndApply()
  }

}
